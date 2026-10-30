package dev.danvega.workshop.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatefulController {

    private final ChatMemory chatMemory;
    private final ChatClient chatClient;

    public StatefulController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        this.chatMemory = chatMemory;
    }

    @GetMapping("/memory")
    public String home(@RequestParam String message,
                       @RequestParam(defaultValue = "default") String conversation) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversation))
                .call()
                .content();
    }

    // uses the chat client

}
