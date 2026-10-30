package dev.danvega.workshop.guards;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PiiGuardController {

    private static final String DEFAULT_MESSAGE = """
            My name is John Doe, my SSN is 123-45-6789 and my email is john.doe@example.com. \
            What should I do if I think my identity was stolen?""";

    private final ChatClient chatClient;

    public PiiGuardController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new PiiMaskingAdvisor(), new PromptLoggingAdvisor())
                .build();
    }

    @GetMapping("/pii-masking")
    public PiiResponse piiMasking(@RequestParam(defaultValue = DEFAULT_MESSAGE) String message) {
        var answer = chatClient.prompt()
                .user(message)
                .call()
                .content();
        return new PiiResponse(message, PiiMaskingAdvisor.mask(message), answer);
    }

    public record PiiResponse(String original, String sentToProvider, String answer) {
    }
}
