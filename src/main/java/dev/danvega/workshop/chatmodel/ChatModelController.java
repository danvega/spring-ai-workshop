package dev.danvega.workshop.chatmodel;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat-model")
public class ChatModelController {

    final ChatModel model;
    // The app's ChatModel uses the Responses API, which has no n option. Multiple
    // generations in one call is a Chat Completions feature, so this demo builds
    // its own Completions-based model. Not a bean: two ChatModel beans would break
    // the ChatClient auto-configuration everywhere else.
    final OpenAiChatModel completionsModel;

    public ChatModelController(ChatModel model, @Value("${spring.ai.openai.api-key}") String apiKey) {
        this.model = model;
        this.completionsModel = OpenAiChatModel.builder()
                .openAiClient(OpenAIOkHttpClient.builder().apiKey(apiKey).build())
                .build();
    }

    /*
    Demo 1: "What did that actually cost?" endpoint. This is a simple example of getting
    more data from the higher level API
     */
    @GetMapping("/fact")
    public Map<String, Object> home() {
        var prompt = new Prompt("Tell me an interesting fact about Orlando, FL");
        var response = model.call(prompt);

        Generation result = response.getResult();
        Usage usage = response.getMetadata().getUsage();

        return Map.of(
                "answer",           result.getOutput().getText(),
                "model",            response.getMetadata().getModel(),
                "finishReason",     result.getMetadata().getFinishReason(),
                "promptTokens",     usage.getPromptTokens(),
                "completionTokens", usage.getCompletionTokens(),
                "totalTokens",      usage.getTotalTokens()
        );

    }

    /*
    Demo 2: Generate several variations in ONE call. ChatClient.call().content() returns a single string,
    so it literally can't show this. ChatModel returns a list of Generations:
    */
    @GetMapping("/taglines")
    public List<String> taglines() {
        var options = OpenAiChatOptions.builder()
                .model("gpt-4o")
                .temperature(1.0)      // increase the creativity
                .n(3)               // ask for 3 completions at once
                .build();

        Prompt prompt = new Prompt("Write a fun tagline for Orlando, FL", options);
        ChatResponse response = completionsModel.call(prompt);

        return response.getResults().stream()
                .map(g -> g.getOutput().getText())
                .toList();
    }

}
