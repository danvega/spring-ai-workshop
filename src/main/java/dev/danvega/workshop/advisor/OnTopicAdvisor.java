package dev.danvega.workshop.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A topic guard that runs before the model. Off-topic questions get a canned answer and
 * the model is never called: no tokens spent, nothing to jailbreak. This is the advisor
 * superpower that logging demos don't show: an advisor can short-circuit the chain and
 * produce the response itself.
 *
 * It is a word check, not mind reading. A real app would combine it with a system prompt
 * and maybe a classifier model. See the famous McDonald's chatbot screenshot in
 * src/main/resources/images for what happens without one.
 */
public class OnTopicAdvisor implements CallAdvisor {

    static final String OFF_TOPIC = "I can only help with our menu, orders, and delivery. "
            + "Try something like \"Is the spicy burger available for delivery?\"";

    /** Phrases people use to get a chatbot to ignore its instructions. */
    private static final List<String> TRICKS = List.of("ignore previous", "ignore all", "ignore your",
            "disregard", "system prompt", "your instructions", "you are now", "pretend you",
            "act as", "jailbreak", "developer mode");

    private static final Set<String> RESTAURANT_WORDS = Set.of("menu", "order", "burger", "fries",
            "nuggets", "shake", "drink", "combo", "meal", "breakfast", "lunch", "dinner", "spicy",
            "vegan", "vegetarian", "allergy", "allergen", "gluten", "nutrition", "calories", "price",
            "cost", "deal", "coupon", "delivery", "pickup", "location", "hours", "open", "refund",
            "missing", "wrong", "cold", "receipt", "app", "reward", "points");

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        var text = request.prompt().getUserMessage().getText();
        if (allowed(text == null ? "" : text)) {
            return chain.nextCall(request);
        }
        var refusal = ChatResponse.builder()
                .generations(List.of(new Generation(new AssistantMessage(OFF_TOPIC))))
                .build();
        return ChatClientResponse.builder().chatResponse(refusal).context(request.context()).build();
    }

    boolean allowed(String question) {
        var lower = question.toLowerCase(Locale.US);
        if (TRICKS.stream().anyMatch(lower::contains)) {
            return false;
        }
        return Arrays.stream(lower.split("[^\\p{L}\\p{N}]+")).anyMatch(RESTAURANT_WORDS::contains);
    }

    @Override
    public String getName() {
        return "OnTopicAdvisor";
    }

    @Override
    public int getOrder() {
        // Before everything else, so a blocked question costs nothing and is not remembered
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
