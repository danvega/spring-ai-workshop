package dev.danvega.workshop.memory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.springaicommunity.agent.tools.AutoMemoryTools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

// Step 2: long-term memory. Markdown files per user, written by the model with AutoMemoryTools.
@RestController
@RequestMapping("/memory/long-term/{userId}")
class LongTermMemoryController {

    private static final String SYSTEM = """
            You are a helpful assistant. Keep answers short.

            You have long-term memory about this user: Markdown files you manage with the Memory tools.
            MEMORY.md is the index.

            Before you answer, call MemoryView on MEMORY.md, then view any file it lists that could help.
            If MEMORY.md does not exist, you have no memories of this user yet.

            When the user tells you something that will matter later, like a trip or how to contact them:
            - Save it in a file, one topic per file, like travel.md. Update the file if it exists.
            - Add one line for the file to MEMORY.md: "- [Title](file.md) - one-line summary".
            - Never save passwords, card numbers, account numbers or one-time codes.
            """;

    // Letters, numbers and dashes only, so "../maria" can't become a folder
    private static final Pattern USER_ID = Pattern.compile("[A-Za-z0-9-]{1,40}");

    private final ChatClient chatClient;
    private final ChatMemoryRepository conversations;
    private final Path root;

    LongTermMemoryController(ChatClient.Builder builder, ChatMemory chatMemory, ChatMemoryRepository conversations,
            @Value("${workshop.memory.root:./memories}") Path root) {
        this.chatClient = builder
                .defaultSystem(SYSTEM)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.conversations = conversations;
        this.root = root.toAbsolutePath().normalize();
    }

    @PostMapping
    ChatReply chat(@PathVariable String userId, @RequestBody ChatRequest request) {
        requireValid(userId);
        String answer = chatClient.prompt()
                .user(request.message())
                // Short-term: this chat
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId + ":" + request.conversationId()))
                // Long-term: this user's folder
                .tools(AutoMemoryTools.builder().memoriesDir(root.resolve(userId)).build())
                .call()
                .content();
        return new ChatReply(answer);
    }

    // Deletes the user's folder and every chat
    @DeleteMapping
    void forget(@PathVariable String userId) throws IOException {
        requireValid(userId);
        FileSystemUtils.deleteRecursively(root.resolve(userId));
        conversations.findConversationIds().stream()
                .filter(id -> id.startsWith(userId + ":"))
                .forEach(conversations::deleteByConversationId);
    }

    private static void requireValid(String userId) {
        if (!USER_ID.matcher(userId).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad user ID: " + userId);
        }
    }

    record ChatRequest(String conversationId, String message) {
    }

    record ChatReply(String answer) {
    }

}
