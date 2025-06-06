package dev.danvega.workshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/posts/new")
    public String newPost(@RequestParam(value = "topic", defaultValue = "JDK Virtual Threads") String topic) {

        // A system message in LLMs is a special type of input that provides high-level instructions, context, or behavioral
        // guidelines to the model before it processes user queries. Think of it as the "behind-the-scenes"
        // instructions that shape how the AI should respond.
        //
        // Use it as a guide or a restriction to the model's behavior
        var system = """
                Blog Post Generator Guidelines:
                
                1. Length & Purpose: Generate 500-word blog posts that inform and engage general audiences.
                
                2. Structure:
                   - Introduction: Hook readers and establish the topic's relevance
                   - Body: Develop 3 main points with supporting evidence and examples
                   - Conclusion: Summarize key takeaways and include a call-to-action
                
                3. Content Requirements:
                   - Include real-world applications or case studies
                   - Incorporate relevant statistics or data points when appropriate
                   - Explain benefits/implications clearly for non-experts
                
                4. Tone & Style:
                   - Write in an informative yet conversational voice
                   - Use accessible language while maintaining authority
                   - Break up text with subheadings and short paragraphs
                
                5. Response Format: Deliver complete, ready-to-publish posts with a suggested title.
                """;

        return chatClient.prompt()
                .system(system)
                .user(u -> {
                    u.text("Write me a blog post about {topic}");
                    u.param("topic",topic);
                })
                .call()
                .content();
    }

}
