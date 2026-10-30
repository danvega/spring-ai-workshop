package dev.danvega.workshop.guards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;

/**
 * Logs the exact user text on its way to the model provider. It changes nothing.
 * It is here so you can SEE what leaves your app. Without a masking rail the
 * customer's SSN goes to the provider in cleartext, even when the model is
 * well-behaved enough not to repeat it back.
 */
public class PromptLoggingAdvisor implements CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(PromptLoggingAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        String outbound = request.prompt().getUserMessage().getText();
        log.info(">> OUTBOUND to provider: {}", outbound);
        return chain.nextCall(request);
    }

    @Override
    public String getName() {
        return "PromptLoggingAdvisor";
    }

    @Override
    public int getOrder() {
        // Run last among input-side advisors so we log what actually leaves,
        // after the masking rail has had its say.
        return Ordered.LOWEST_PRECEDENCE;
    }
}
