package dev.danvega.workshop;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleChatController {

    private final ChatClient chatClient;

    public SimpleChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/java")
    public String chat() {
        return chatClient.prompt()
                .user("Tell me an intresting fact about Java")
                .call()
                .content();
    }

}
