package dev.danvega.workshop.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A restaurant support bot that stays on topic. Ask about the menu and the model
 * answers. Ask it to reverse a linked list and the OnTopicAdvisor answers instead,
 * without a model call.
 */
@RestController
public class SupportController {

    private final ChatClient chatClient;

    public SupportController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a friendly support bot for a burger restaurant. Keep answers short.")
                .defaultAdvisors(new OnTopicAdvisor())
                .build();
    }

    @GetMapping("/support")
    public String support(@RequestParam(defaultValue = "Is the spicy burger available for delivery?") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
