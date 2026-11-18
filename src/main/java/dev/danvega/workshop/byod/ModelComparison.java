package dev.danvega.workshop.byod;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelComparison {

    private final ChatClient chatClient;

    public ModelComparison(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /*
     * How can we improve this?
     * We could send the context along with the request, but we would have to do that every single time.
     *   - we are paying for those tokens even when the question has nothing to do with that
     */
    @GetMapping("/models")
    public String models() {
        return chatClient.prompt()
                .user("Can you give me an up to date list of popular large language models and their current context window size")
                .call()
                .content();
    }

    @GetMapping("/models/stuff-the-prompt")
    public String modelsWithData() {
        // Checked November 18, 2026
        var system = """
                If you're asked about up to date language models and their context windows, here is some information to help you with your response:
                [
                  {
                    "company": "OpenAI",
                    "model": "GPT-6 Astra",
                    "context_window_size": 1050000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-6.1 Sol",
                    "context_window_size": 1050000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-6.1 Terra",
                    "context_window_size": 1050000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-6 Luna",
                    "context_window_size": 1050000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Fable 5.2",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Opus 5.5",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Sonnet 5.5",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Haiku 5.5",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 4 Argon",
                    "context_window_size": 1048576
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 3.8 Flash",
                    "context_window_size": 1048576
                  },
                  {
                    "company": "xAI",
                    "model": "Grok 4.7",
                    "context_window_size": 500000
                  },
                  {
                    "company": "Meta AI",
                    "model": "Llama 4 Scout",
                    "context_window_size": 10000000
                  },
                  {
                    "company": "Meta AI",
                    "model": "Llama 4 Maverick",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "DeepSeek",
                    "model": "DeepSeek V4 Pro",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Alibaba Cloud",
                    "model": "Qwen 3.5",
                    "context_window_size": 262144
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mistral Large 3",
                    "context_window_size": 256000
                  }
                ]
                """;
        return chatClient.prompt()
                .user("Can you give me an up to date list of popular large language models and their current context window size")
                .system(system)
                .call()
                .content();
    }
}
