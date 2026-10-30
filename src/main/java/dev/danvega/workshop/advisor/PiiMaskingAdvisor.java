package dev.danvega.workshop.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Masks PII in the user message before it leaves the app. The model provider only
 * ever sees the placeholders. Runs early so every later advisor, and the provider,
 * works with the masked text.
 */
public class PiiMaskingAdvisor implements CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(PiiMaskingAdvisor.class);

    private static final Map<Pattern, String> PATTERNS = Map.of(
            Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b"), "[SSN]",
            Pattern.compile("\\b[\\w.+-]+@[\\w-]+\\.[\\w.]+\\b"), "[EMAIL]",
            Pattern.compile("\\b\\d{3}[-.]\\d{3}[-.]\\d{4}\\b"), "[PHONE]"
    );

    public static String mask(String text) {
        var masked = text;
        for (var entry : PATTERNS.entrySet()) {
            masked = entry.getKey().matcher(masked).replaceAll(entry.getValue());
        }
        return masked;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        var original = request.prompt().getUserMessage().getText();
        var masked = mask(original);
        if (!masked.equals(original)) {
            log.info("Masked PII before the prompt left the app");
        }
        return chain.nextCall(request.mutate()
                .prompt(request.prompt().augmentUserMessage(masked))
                .build());
    }

    @Override
    public String getName() {
        return "PiiMaskingAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
