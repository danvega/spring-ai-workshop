package dev.danvega.workshop.options;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

    private final ChatClient chatClient;

    public OptionsController(ChatClient.Builder builder) {

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-5")
                .temperature(1.0)
                .maxCompletionTokens(500)
                .build();

        this.chatClient = builder
                .defaultOptions(options)
                .build();
    }

    @GetMapping("/options")
    public ChatResponse options() {
        return chatClient.prompt()
                .user("Tell me an interesting fact about QCon")
                .call()
                .chatResponse();
    }

}
