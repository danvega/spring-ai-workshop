# Spring AI Workshop

Welcome to the Spring AI Workshop repository! This project demonstrates how to build intelligent applications in Java using Spring AI. Whether you're new to AI or looking to integrate advanced capabilities into your Spring applications, this workshop will guide you through essential concepts and practical implementations.

## What You'll Learn

This workshop covers both theoretical foundations and hands-on coding examples, including:
- Understanding AI, Machine Learning, and Deep Learning
- Working with Large Language Models (LLMs)
- Building AI-powered applications with Spring AI
- Implementing chat interfaces, memory management, and structured outputs
- Leveraging techniques like RAG (Retrieval Augmented Generation)
- Working with multimodal AI capabilities

## Agenda

- **Artificial Intelligence (AI)**
  - Overview
  - Machine Learning / Deep Learning
    - Supervised Learning
    - Unsupervised Learning
- **Large Language Models (LLMs)**
  - Models
  - API Keys
  - Tokens
  - Calling REST Endpoints
- **Spring AI**
  - Getting Started
  - Chat Client
  - Memory
  - Prompt Engineering
  - Structured Output
  - RAG (Retrieval Augmented Generation)
  - Tools & Function Calling
  - Multimodal Capabilities

## Artificial Intelligence (AI)

It's fair to say that the term "AI" is frequently used today. But what exactly is it? Artificial Intelligence is a scientific field that emerged shortly after World War 2. For quite some time, progress in this area was rather slow.

AI is a broad discipline that includes various techniques. In this workshop, we will focus on a subset of these techniques. We will begin with Machine Learning, specifically Deep Learning, which involves the use of Neural Networks. The emphasis in this field is on learning, or acquiring skills or knowledge from experience. For the purpose of our discussion, we can categorize this into supervised and unsupervised learning.

![Supervised vs Unsupervised Learning](/images/supervised_unsupervised.png)

### Supervised Learning

Supervised learning is a machine learning approach where the model is trained using a labeled dataset. In this context, every training example is associated with an output label. The aim of supervised learning is to establish a link between inputs and outputs, which can then be utilized to predict labels for new, unseen data.

As their name suggests, they 'literally' divide data points into different classes and learn the boundaries using probability estimates and maximum likelihood. Here are some use cases for supervised machine learning that you may have encountered:

- Facial Recognition
- Recognize Tumors on x-ray scans
- Abnormality on ultrasounds
- Self-driving mode (recognize stop sign / pedestrian / etc)
- Fraud detection
- Product Recommendations (YouTube)
- Spam Filtering

### Unsupervised Learning

Unsupervised learning is a type of machine learning where the model is trained on data without labeled responses. In other words, the algorithm is given a dataset with input data but without any corresponding output values. The goal of unsupervised learning is to find hidden patterns or intrinsic structures in the input data.

**Large Language Models (LLMs)** are trained on vast amounts of training data and this is powered by lots of compute power. LLMs use unsupervised or self-supervised learning during their training phase. They predict the next word in a sentence (or fill in blanks) using context from the surrounding text, learning from the structure and patterns within the data itself without explicit labels.

**Generative AI** is based on the transformer architecture which is able to take that training data and generate something brand new at the intersection of LLMs. Generative AI is a broader concept encompassing various techniques and models, including but not limited to LLMs. Generative AI is what powers applications like Open AI's GPT & Google Gemini.

![AI](/images/ai.png)

## Project Requirements

To run this workshop code, you'll need:

- Java 17 or higher
- Maven or Gradle (project uses Maven)
- An OpenAI API key (for accessing GPT models)
- Spring Boot 3.2+

## Dependencies

This project relies on several key dependencies:

- Spring Boot 3.2+
- Spring AI
- Spring Web
- Spring AI OpenAI Starter
- Spring DevTools (optional, for development)

## Getting Started

### Setting Up Your Environment

1. Clone this repository
2. Ensure you have Java 17+ installed
3. Create an OpenAI API key at [OpenAI Platform](https://platform.openai.com/)
4. Configure your application.properties:

```properties
spring.application.name=ai-workshop
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o
spring.threads.virtual.enabled=true
```

For security, it's recommended to use environment variables for your API key rather than hardcoding it.

### HTTP Clients for Testing

To test the REST endpoints, you'll need an HTTP client. Options include:

- [IntelliJ HTTP Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html)
- [Postman](https://www.postman.com/)
- [cURL](https://curl.se/)
- [HTTPie](https://httpie.io/)

## How to Run the Application

1. Set your OpenAI API key as an environment variable:
   ```
   export OPENAI_API_KEY=your-api-key-here
   ```

2. Start the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```

3. The application will start, typically on port 8080

## Core Concepts and Code Examples

### Working with the Chat Client

The ChatClient is a key component in Spring AI, providing a fluent API for interacting with LLMs. Here's a basic example from our `ChatController`:

```java
@GetMapping("/")
public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about Dogs") String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

You can also parametrize messages:

```java
@GetMapping("/jokes-by-topic")
public String jokesByTopic(@RequestParam String topic) {
    return chatClient.prompt()
            .user(u -> u.text("Tell me a joke about {topic}").param("topic",topic))
            .call()
            .content();
}
```

To access the full response object:

```java
@GetMapping("jokes-with-response")
public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about computers") String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .chatResponse();
}
```

### Streaming Responses

For longer responses, you can stream results to improve user experience:

```java
@GetMapping("/stream")
public Flux<String> stream() {
    return chatClient.prompt()
            .user("I am visiting Atlanta, GA can you give me 10 places I must visit")
            .stream()
            .content();
}
```

### Implementing Chat Memory

LLMs are stateless by default. Spring AI provides memory capabilities to maintain conversation context:

```java
public StatefulController(ChatClient.Builder builder) {
    this.chatClient = builder
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .build();
}

@GetMapping("/chat")
public String home(@RequestParam String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

Test conversation memory with these sequential requests:

```shell
http :8080/chat message=="My name is Dan, how are you doing today?"
http :8080/chat message=="What is my name?"
```

### Structured Output

Spring AI can automatically convert LLM responses to Java objects:

```java
@GetMapping("/vacation/structured")
public Itinerary vacationStructured() {
    return chatClient.prompt()
            .user("What's a good vacation plan while I'm in Montreal CA for 4 days?")
            .call()
            .entity(Itinerary.class);
}

record Activity(String activity, String location, String day, String time){}
record Itinerary(List<Activity> itinerary) {}
```

### Retrieval Augmented Generation (RAG)

RAG enhances LLM responses by incorporating your own data. The `ModelsController` demonstrates this:

```java
public ModelsController(ChatClient.Builder builder, VectorStore vectorStore) {
    this.chatClient = builder
            .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
            .build();
}

@GetMapping("/rag/models")
public String faq(@RequestParam(value = "message", defaultValue = "List the top 3 Large Language Models when it comes to context window size.") String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

The `RagConfiguration` class handles document loading and vector storage:

```java
@Bean
SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
    var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    var vectorStoreFile = getVectorStoreFile();
    if (vectorStoreFile.exists()) {
        log.info("Vector Store File Exists,");
        simpleVectorStore.load(vectorStoreFile);
    } else {
        log.info("Vector Store File Does Not Exist, loading documents");
        TextReader textReader = new TextReader(models);
        // rest of configuration...
    }
    return simpleVectorStore;
}
```

### Working with Tools and Function Calling

Spring AI supports tools (function calling) to extend LLM capabilities:

```java
@GetMapping("/tools")
public String tools() {
    return chatClient.prompt("What day is tomorrow?")
            .tools(new DatTimeTools())
            .call()
            .content();
}

// Tool definition
public class DatTimeTools {
    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
```

### Multimodal Capabilities

Spring AI supports working with multiple content types, including images:

```java
@GetMapping("/image-to-text")
public String image() throws IOException {
    return chatClient.prompt()
            .user(u -> u
                    .text("Can you please explain what you see in the following image?")
                    .media(MimeTypeUtils.IMAGE_JPEG, sampleImage)
            )
            .call()
            .content();
}
```

### Effective Prompt Engineering

Carefully crafted prompts can dramatically improve LLM outputs. Here's an example from `ArticleController`:

```java
@GetMapping("/posts/new")
public String newPost(@RequestParam String topic) {
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
```

### Calling LLM APIs Directly

For comparison, here's how to call OpenAI directly without Spring AI:

```java
public static void main(String[] args) throws IOException, InterruptedException {
    var apiKey = System.getenv("OPENAI_API_KEY");
    var body = """
            {
                "model": "gpt-4o",
                "messages": [
                    {
                        "role": "user",
                        "content": "Tell me an interesting fact about the Spring Framework"
                    }
                ]
            }""";

    var request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

    var client = HttpClient.newHttpClient();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
}
```

## Bringing Your Own Data

Three techniques exist for customizing AI models with your data:

1. **Fine Tuning**: Tailoring the model by changing its internal weighting. Resource-intensive and challenging.

2. **Prompt Stuffing**: Embedding your data within the prompt. Example from `ModelComparison`:

```java
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
              ...
            ]
            """;
    return chatClient.prompt()
            .user("Give me 1 llm per company with the largest context window")
            .system(system)
            .call()
            .content();
}
```

3. **Function Calling**: Registering custom functions that connect LLMs to external systems.

## Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring Initializr](https://start.spring.io)
- [OpenAI API Documentation](https://platform.openai.com/docs/introduction)
- [Prompt Engineering Guidelines](https://platform.openai.com/docs/guides/prompt-engineering)

## Conclusion

Spring AI provides a powerful, intuitive way to integrate AI capabilities into your Java applications. By leveraging the familiar Spring ecosystem, developers can quickly build sophisticated AI-powered features without needing extensive machine learning expertise.

This workshop repository demonstrates fundamental techniques for working with LLMs in Spring, from basic chat interactions to advanced RAG implementations and multimodal capabilities. As you explore these examples, you'll gain practical skills for creating your own AI-enhanced applications.

Happy coding!