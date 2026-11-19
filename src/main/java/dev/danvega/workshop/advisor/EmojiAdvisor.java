package dev.danvega.workshop.advisor;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EmojiAdvisor implements CallAdvisor {

    private static final List<String> EMOJIS = List.of("🚀", "🎉", "☕", "🔥", "🌮", "🦄", "🍕", "🤖");

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        var emoji = EMOJIS.get(ThreadLocalRandom.current().nextInt(EMOJIS.size()));
        var original = request.prompt().getUserMessage().getText();

        return chain.nextCall(request.mutate()
                .prompt(request.prompt().augmentUserMessage(original + "\n\nEnd your answer with this emoji: " + emoji))
                .build());
    }

    @Override
    public @NonNull String getName() {
        return "EmojiAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
