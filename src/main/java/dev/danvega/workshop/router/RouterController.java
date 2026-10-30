package dev.danvega.workshop.router;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.responses.OpenAiResponsesChatOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(RouterProperties.class)
public class RouterController {

    private final ChatClient chatClient;
    private final ModelRouter router;

    public RouterController(ChatClient.Builder builder, ModelRouter router) {
        this.chatClient = builder.build();
        this.router = router;
    }

    @GetMapping("/router")
    public RoutedAnswer route(@RequestParam(defaultValue = "What is the capital of Ohio?") String message) {
        var decision = router.route(message);
        var answer = chatClient.prompt()
                .user(message)
                .options(OpenAiResponsesChatOptions.builder().model(decision.model().id()))
                .call()
                .content();
        return new RoutedAnswer(decision.model().id(), decision.model().tier(), decision.confidence(),
                decision.fallback(), decision.reason(), answer);
    }

    public record RoutedAnswer(String model, RouterProperties.Tier tier, Double confidence,
                               boolean fallback, String reason, String answer) {
    }
}
