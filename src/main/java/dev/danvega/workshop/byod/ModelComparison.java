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
     *   - we are paying for those tokens even when the question has nothing do with that
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
        var system = """
                If you're asked about up to date language models and there context window here is some information to help you with your response: 
                [
                  {
                    "company": "OpenAI",
                    "model": "GPT-4o",
                    "context_window_size": 128000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4o mini",
                    "context_window_size": 128000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-4 Turbo",
                    "context_window_size": 128000
                  },
                  {
                    "company": "OpenAI",
                    "model": "o1-preview",
                    "context_window_size": 128000
                  },
                  {
                    "company": "OpenAI",
                    "model": "o1",
                    "context_window_size": 128000
                  },
                  {
                    "company": "OpenAI",
                    "model": "GPT-5",
                    "context_window_size": 400000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Sonnet 4.5",
                    "context_window_size": 200000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Opus 4.1",
                    "context_window_size": 200000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude Haiku 4.5",
                    "context_window_size": 200000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude 3.5 Sonnet",
                    "context_window_size": 200000
                  },
                  {
                    "company": "Anthropic",
                    "model": "Claude 3 Opus",
                    "context_window_size": 200000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.5 Pro",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.5 Flash",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.0 Pro",
                    "context_window_size": 2000000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 2.0 Flash",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Google",
                    "model": "Gemini 3 Pro",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Meta AI",
                    "model": "Llama 3.3 70B",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Meta AI",
                    "model": "Llama 3.2 3B",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Meta AI",
                    "model": "Llama 3.1 405B",
                    "context_window_size": 128000
                  },
                  {
                    "company": "xAI",
                    "model": "Grok 4 Fast",
                    "context_window_size": 2000000
                  },
                  {
                    "company": "xAI",
                    "model": "Grok 4",
                    "context_window_size": 256000
                  },
                  {
                    "company": "xAI",
                    "model": "Grok 2",
                    "context_window_size": 131072
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mistral Large 2",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mistral Small 3.1",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mistral NeMo 12B",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mixtral 8x22B",
                    "context_window_size": 64000
                  },
                  {
                    "company": "Mistral AI",
                    "model": "Mixtral 8x7B",
                    "context_window_size": 32000
                  },
                  {
                    "company": "Alibaba Cloud",
                    "model": "Qwen2.5-Max",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Alibaba Cloud",
                    "model": "Qwen2.5-7B-Instruct-1M",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Alibaba Cloud",
                    "model": "Qwen2.5-14B-Instruct-1M",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Alibaba Cloud",
                    "model": "Qwen2.5 72B",
                    "context_window_size": 128000
                  },
                  {
                    "company": "DeepSeek",
                    "model": "DeepSeek V3.1",
                    "context_window_size": 128000
                  },
                  {
                    "company": "DeepSeek",
                    "model": "DeepSeek R1",
                    "context_window_size": 128000
                  },
                  {
                    "company": "DeepSeek",
                    "model": "DeepSeek V3",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Cohere",
                    "model": "Command A",
                    "context_window_size": 256000
                  },
                  {
                    "company": "Cohere",
                    "model": "Command R+",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Cohere",
                    "model": "Command R",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Databricks",
                    "model": "DBRX Instruct",
                    "context_window_size": 32000
                  },
                  {
                    "company": "AI21 Labs",
                    "model": "Jamba 1.6",
                    "context_window_size": 256000
                  },
                  {
                    "company": "AI21 Labs",
                    "model": "Jamba 1.5 Large",
                    "context_window_size": 256000
                  },
                  {
                    "company": "IBM",
                    "model": "Granite 3B/8B Long",
                    "context_window_size": 128000
                  },
                  {
                    "company": "Amazon AWS",
                    "model": "Amazon Nova Premier",
                    "context_window_size": 1000000
                  },
                  {
                    "company": "Amazon AWS",
                    "model": "Amazon Nova Pro",
                    "context_window_size": 300000
                  },
                  {
                    "company": "Amazon AWS",
                    "model": "Amazon Nova Lite",
                    "context_window_size": 300000
                  },
                  {
                    "company": "Amazon AWS",
                    "model": "Amazon Nova Micro",
                    "context_window_size": 128000
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
