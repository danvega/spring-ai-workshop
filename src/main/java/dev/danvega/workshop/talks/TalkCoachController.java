package dev.danvega.workshop.talks;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Lab 3: advisors (memory and logging) and a tool, on top of the talk reviewer from lab 2
@RestController
public class TalkCoachController {

    private final ChatClient chatClient;

    public TalkCoachController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a friendly coach who helps developers improve conference talk submissions.
                        Keep answers short. When you suggest a new title or description, show the full new version.
                        Use the countWords tool for any question about length. Never guess a word count.
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor())
                .build();
    }

    @PostMapping("/talks/coach")
    public String coach(@RequestBody String message,
                        @RequestParam(defaultValue = "default") String conversation) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversation))
                .tools(new WordCountTools())
                .call()
                .content();
    }
}
