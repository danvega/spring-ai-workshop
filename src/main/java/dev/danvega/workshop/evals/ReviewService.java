package dev.danvega.workshop.evals;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.responses.OpenAiResponsesChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ChatClient chatClient;

    public ReviewService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultOptions(OpenAiResponsesChatOptions.builder().reasoningEffort("low"))
                .build();
    }

    public Sentiment classifySentiment(String review) {
        String systemPrompt = """
                Classify the sentiment of the following text as POSITIVE, NEGATIVE, or NEUTRAL. \
                Respond in the requested format.""";

        return chatClient.prompt()
                .system(systemPrompt)
                .user(review)
                .call()
                .entity(Sentiment.class);
    }

}
