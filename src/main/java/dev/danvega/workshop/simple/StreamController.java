package dev.danvega.workshop.simple;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/without-stream")
    public String withoutStream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting Dallas this week, what are 10 places I must visit?") String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // http --stream :8080/stream
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting Dallas this week, what are 10 places I must visit?") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

}
