package dev.danvega.workshop.evals;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class ChatClientTestConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }


    @Bean
    public FactCheckingEvaluator factCheckingEvaluator(ChatClient.Builder chatClientBuilder) {
        return FactCheckingEvaluator.builder(chatClientBuilder)
                .build();
    }
}
