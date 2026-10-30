package dev.danvega.workshop.options;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.responses.OpenAiResponsesChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

    private final ChatClient chatClient;

    public OptionsController(ChatClient.Builder builder) {

        OpenAiResponsesChatOptions.Builder options = OpenAiResponsesChatOptions.builder()
                .model("gpt-5.4-mini")
                .reasoningEffort("low")
                .maxOutputTokens(500);

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
