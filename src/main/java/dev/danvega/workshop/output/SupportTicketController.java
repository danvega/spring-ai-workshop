package dev.danvega.workshop.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * New in Spring AI 2: structured output the provider enforces, and output that checks itself.
 * Send a plain text email, or no body to use the sample.
 */
@RestController
public class SupportTicketController {

    private static final String SAMPLE_EMAIL = """
            hi,, this is the THIRD time im writing. Jordan Lee here.
            you charged my card twice on nov 2 and i want one of those back.
            also the export button just spins forever. please fix asap!!
            """;

    private final ChatClient chatClient;

    public SupportTicketController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // The schema goes to OpenAI as a strict json_schema response format.
    // The API guarantees the shape, so nothing is added to the prompt.
    @PostMapping("/output/ticket/provider")
    public SupportTicket provider(@RequestBody(required = false) String email) {
        return chatClient.prompt()
                .user(emailOrSample(email))
                .call()
                .entity(SupportTicket.class, spec -> spec.useProviderStructuredOutput());
    }

    // Spring AI checks the JSON against the schema. If it fails, it sends the error
    // back to the model and tries again, up to 3 more times.
    @PostMapping("/output/ticket/validated")
    public SupportTicket validated(@RequestBody(required = false) String email) {
        return chatClient.prompt()
                .user(emailOrSample(email))
                .call()
                .entity(SupportTicket.class, spec -> spec.validateSchema());
    }

    private static String emailOrSample(String email) {
        return email == null || email.isBlank() ? SAMPLE_EMAIL : email;
    }
}
