package dev.danvega.workshop.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController {

    private final ChatClient chatClient;

    public LoggingController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // SimpleLoggerAdvisor logs the request and response at DEBUG (see application.yml)
    @GetMapping("/advisor/logging")
    public String simpleLogger() {
        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user("In one sentence, what is Spring AI?")
                .call()
                .content();
    }

    @GetMapping("/advisor")
    public String logging() {
        return chatClient.prompt()
                .advisors(new TokenCounterAdvisor())
                .user("How long would it take to drive from Cleveland OH to San Francisco, CA?")
                .call()
                .content();
    }

}
