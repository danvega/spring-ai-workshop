package dev.danvega.workshop;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Primary
	public ChatModel primaryChatModel(OpenAiChatModel openAiChatModel) {
		return openAiChatModel;
	}

	@Bean
	@Primary
	public EmbeddingModel primaryEmbeddingModel(OpenAiEmbeddingModel openAiEmbeddingModel) {
		return openAiEmbeddingModel;
	}
}
