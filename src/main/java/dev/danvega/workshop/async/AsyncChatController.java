package dev.danvega.workshop.async;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AsyncChatController {

    private final ChatClient chatClient;

    public AsyncChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/stream")
    public Flux<String> stream() {
        return chatClient.prompt()
                .user("I am visiting Charleston, SC can you give me 10 places I must visit")
                .stream()
                .content();
    }

}
