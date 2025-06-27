# Spring AI Workshop: Every Java Developer is now an AI Developer

**Transform your Java skills for the AI era with this comprehensive Spring AI masterclass!**

Are you a Java developer ready to harness the power of AI in your applications? This complete workshop takes you from AI fundamentals to building production-ready intelligent applications using Spring AI 1.0.

This repository contains all the source code, examples, and resources from the comprehensive 5.5-hour Spring AI course, designed to transform Java developers into AI-powered application builders.

## 🎯 What You'll Master

### Foundation & Theory
- AI fundamentals: Machine Learning, Deep Learning, and LLM architecture
- Prompt engineering mastery - the most critical skill for AI developers
- Model selection strategies and cost optimization with tokens

### Spring AI Implementation
- Chat clients with streaming responses and memory management
- Structured outputs and multimodal processing (images, audio)
- Multiple AI model integration in single applications
- Prompt templates and advanced configuration

### Overcoming LLM Limitations
- Retrieval Augmented Generation (RAG) for enhanced accuracy
- Custom tool development and function calling
- Model Context Protocol (MCP) for reusable integrations
- Prompt guarding and security best practices

### Production Excellence
- Open-source vs proprietary model comparison
- Running local models with Ollama and Docker
- Observability with Prometheus and Grafana
- Testing strategies for non-deterministic AI systems

## 🛠 Hands-On Workshop

You'll build:
- Intelligent chatbots with conversation memory
- Document analysis systems using RAG
- Custom AI tools and MCP servers
- Multimodal applications processing text, images, and audio

## 📋 Prerequisites

- **Java 21** or higher (updated from Java 17)
- **Maven** for dependency management
- **Docker Desktop** (required for local LLM instances)
- **OpenAI API key** (for cloud models)
- Basic familiarity with Spring Framework
- **No machine learning background required!**

## 🚀 Getting Started

### 1. Clone and Setup

```bash
git clone https://github.com/danvega/spring-ai-workshop.git
cd spring-ai-workshop
```

### 2. Environment Configuration

Create your environment variables:

```bash
export OPENAI_API_KEY=your-api-key-here
```

### 3. Install Ollama (for local models)

```bash
# macOS
brew install ollama

# Start Ollama service
ollama serve

# Pull required models
ollama pull bespokeai/minicheck
```

### 4. Application Configuration

Configure `src/main/resources/application.properties`:

```properties
spring.application.name=ai-workshop
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o
spring.threads.virtual.enabled=true

# Local Ollama configuration
spring.ai.ollama.chat.model=bespokeai/minicheck
spring.ai.ollama.chat.temperature=0.1
```

### 5. Run the Application

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`

## 🏗 Project Structure

### Core Dependencies

- **Spring Boot 3.3.0**
- **Spring AI 1.0.0** (latest stable release)
- **Spring AI OpenAI Starter**
- **Spring AI Vector Store Advisors**
- **Spring Boot Actuator** (for metrics)
- **Testcontainers** (for testing)

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-advisors-vector-store</artifactId>
</dependency>
```

## 📚 Course Content & Code Examples

### Introduction & Setup

**Getting Started: Your First AI Application** [`SimpleChatController.java`](src/main/java/dev/danvega/workshop/SimpleChatController.java)

```java
@RestController
public class SimpleChatController {
    private final ChatClient chatClient;
    
    public SimpleChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    @GetMapping("/")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about Dogs") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

### AI Fundamentals

#### Understanding AI, Machine Learning & Deep Learning

AI is a broad discipline that includes various techniques. This workshop focuses on Deep Learning using Neural Networks, specifically Large Language Models (LLMs).

![AI Overview](/images/ai.png)

**Supervised vs Unsupervised Learning:**

![Supervised vs Unsupervised Learning](/images/supervised_unsupervised.png)

**Large Language Models (LLMs)** are trained using unsupervised learning on vast amounts of text data, learning to predict the next word in a sequence based on context.

#### Prompt Engineering Fundamentals

Effective prompting is crucial for getting quality responses from LLMs. Example from [`ArticleController.java`](src/main/java/dev/danvega/workshop/prompt/ArticleController.java):

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
            4. Tone & Style:
               - Write in an informative yet conversational voice
               - Use accessible language while maintaining authority
            5. Response Format: Deliver complete, ready-to-publish posts with a suggested title.
            """;

    return chatClient.prompt()
            .system(system)
            .user(u -> {
                u.text("Write me a blog post about {topic}");
                u.param("topic", topic);
            })
            .call()
            .content();
}
```

### Spring AI Core Features

#### Chat Clients & Streaming Responses

**Basic Chat Implementation** [`ChatController.java`](src/main/java/dev/danvega/workshop/chat/ChatController.java):

```java
@GetMapping("/jokes-by-topic")
public String jokesByTopic(@RequestParam String topic) {
    return chatClient.prompt()
            .user(u -> u.text("Tell me a joke about {topic}").param("topic", topic))
            .call()
            .content();
}
```

**Streaming for Long Responses:**

```java
@GetMapping("/stream")
public Flux<String> stream() {
    return chatClient.prompt()
            .user("I am visiting Atlanta, GA can you give me 10 places I must visit")
            .stream()
            .content();
}
```

**Accessing Full Response Object:**

```java
@GetMapping("jokes-with-response")
public ChatResponse jokeWithResponse(@RequestParam String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .chatResponse();
}
```

#### Structured Output with Type Safety

Spring AI automatically converts LLM responses to Java objects [`VacationPlan.java`](src/main/java/dev/danvega/workshop/output/VacationPlan.java):

```java
@GetMapping("/vacation/structured")
public Itinerary vacationStructured() {
    return chatClient.prompt()
            .user("What's a good vacation plan while I'm in Montreal CA for 4 days?")
            .call()
            .entity(Itinerary.class);
}

record Activity(String activity, String location, String day, String time) {}
record Itinerary(List<Activity> itinerary) {}
```

#### Multimodal AI: Images & Audio Processing

**Image Analysis** [`ImageDetection.java`](src/main/java/dev/danvega/workshop/multimodal/image/ImageDetection.java):

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

**Image Generation** [`ImageGeneration.java`](src/main/java/dev/danvega/workshop/multimodal/image/ImageGeneration.java):

```java
@GetMapping("/generate-image")
public String generateImage(@RequestParam String prompt) {
    var imageResponse = imageModel.call(new ImagePrompt(prompt));
    return imageResponse.getResult().getOutput().getUrl();
}
```

#### Chat Memory & Conversation State

Maintain context across requests [`StatefulController.java`](src/main/java/dev/danvega/workshop/memory/StatefulController.java):

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

Test conversation memory:
```bash
curl "http://localhost:8080/chat?message=My name is Dan"
curl "http://localhost:8080/chat?message=What is my name?"
```

### Overcoming LLM Limitations

#### Prompt Guarding & Security

**Input Validation Guard** [`InputValidationGuardController.java`](src/main/java/dev/danvega/workshop/guards/InputValidationGuardController.java):

```java
@GetMapping("/input-validation")
public String inputValidation(@RequestParam String userInput) {
    var system = """
        You are a content filter. Analyze the user input and respond with only:
        - "SAFE" if the input is appropriate
        - "UNSAFE" if the input contains harmful, offensive, or inappropriate content
        """;
    
    String validation = chatClient.prompt()
            .system(system)
            .user(userInput)
            .call()
            .content();
    
    if ("UNSAFE".equals(validation.trim())) {
        return "I cannot process that request due to content policy violations.";
    }
    
    return chatClient.prompt()
            .user(userInput)
            .call()
            .content();
}
```

**Fact-Checking Guard** [`FactCheckingGuardController.java`](src/main/java/dev/danvega/workshop/guards/FactCheckingGuardController.java):

```java
@GetMapping("/fact-checking")
public String factChecking() {
    var system = """
        You are a research assistant. You must follow these rules strictly:
        
        NEVER provide specific numbers, percentages, dates, or statistics unless you are 100% certain they are correct.
        
        For questions asking for:
        - Specific statistics or percentages → Always respond: "I cannot provide specific statistics without access to current data sources"
        - Product feature details → Always respond: "I don't have access to current product documentation"  
        - Research paper details → Always respond: "I cannot cite specific papers without verification"
        - Financial figures → Always respond: "I don't have access to current financial data"
        """;
    return chatClient.prompt()
            .system(system)
            .user("How many GitHub stars does the Spring Boot repository have as of today?")
            .call()
            .content();
}
```

#### Retrieval Augmented Generation (RAG)

Enhance responses with your own data [`ModelsController.java`](src/main/java/dev/danvega/workshop/rag/ModelsController.java):

```java
public ModelsController(ChatClient.Builder builder, VectorStore vectorStore) {
    this.chatClient = builder
            .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
            .build();
}

@GetMapping("/rag/models")
public String faq(@RequestParam String message) {
    return chatClient.prompt()
            .user(message)
            .call()
            .content();
}
```

**RAG Configuration** [`RagConfiguration.java`](src/main/java/dev/danvega/workshop/rag/RagConfiguration.java):

```java
@Bean
SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
    var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    var vectorStoreFile = getVectorStoreFile();
    if (vectorStoreFile.exists()) {
        log.info("Vector Store File Exists, loading from file");
        simpleVectorStore.load(vectorStoreFile);
    } else {
        log.info("Vector Store File Does Not Exist, loading documents");
        TextReader textReader = new TextReader(models);
        textReader.getCustomMetadata().put("filename", "models.json");
        List<Document> documents = textReader.get();
        var textSplitter = TokenTextSplitter.builder().withDefaultChunkSize(800).withMinChunkSizeChars(350).withChunkOverlap(200).build();
        List<Document> splitDocuments = textSplitter.apply(documents);
        simpleVectorStore.add(splitDocuments);
        simpleVectorStore.save(vectorStoreFile);
    }
    return simpleVectorStore;
}
```

#### Tools & Function Calling

Extend LLM capabilities with custom tools [`WeatherController.java`](src/main/java/dev/danvega/workshop/tools/weather/WeatherController.java):

```java
@GetMapping("/weather")
public String weather(@RequestParam String city) {
    return chatClient.prompt()
            .user("What is the weather like in {city}", Map.of("city", city))
            .tools(new WeatherTools())
            .call()
            .content();
}
```

**DateTime Tools** [`DatTimeTools.java`](src/main/java/dev/danvega/workshop/tools/datetime/DatTimeTools.java):

```java
public class DatTimeTools {
    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
    
    @Tool(description = "Get tomorrow's date in the user's timezone")
    String getTomorrowsDate() {
        return LocalDate.now().plusDays(1).toString();
    }
}
```

**Task Management Tools** [`TaskManagementTools.java`](src/main/java/dev/danvega/workshop/tools/action/TaskManagementTools.java):

Action-based tools that can modify system state and take concrete actions.

### Open Source Models

#### Running Local Models with Ollama

**Bring Your Own Data** [`ModelComparison.java`](src/main/java/dev/danvega/workshop/byod/ModelComparison.java):

```java
@GetMapping("/models/stuff-the-prompt")
public String modelsWithData() {
    var system = """
            If you're asked about up to date language models and their context window here is some information: 
            [
              {
                "company": "OpenAI",
                "model": "GPT-4o",
                "context_window_size": 128000
              },
              {
                "company": "Anthropic", 
                "model": "Claude 3.5 Sonnet",
                "context_window_size": 200000
              }
            ]
            """;
    return chatClient.prompt()
            .user("Give me 1 llm per company with the largest context window")
            .system(system)
            .call()
            .content();
}
```

### Production & Monitoring

#### Observability with Prometheus & Grafana

Spring Boot Actuator provides built-in metrics for AI operations. See the [Observability README](src/main/java/dev/danvega/workshop/observability/README.md) for detailed setup instructions.

**Key Metrics to Monitor:**
- Token usage and costs
- Response times
- Success/failure rates
- Model performance metrics

#### Testing AI Applications & Model Evaluations

**Sentiment Analysis Testing** [`SentimentAnalysisTest.java`](src/test/java/dev/danvega/workshop/evals/SentimentAnalysisTest.java):

```java
@Test
void shouldAnalyzeSentimentCorrectly() {
    String positiveText = "I love this new feature!";
    
    Sentiment result = chatClient.prompt()
            .user("Analyze the sentiment of this text: " + positiveText)
            .call()
            .entity(Sentiment.class);
            
    assertThat(result).isEqualTo(Sentiment.POSITIVE);
}
```

**Relevancy Evaluation** [`RelevancyEvaluatorTest.java`](src/test/java/dev/danvega/workshop/evals/RelevancyEvaluatorTest.java):

```java
@Test
void shouldEvaluateResponseRelevancy() {
    var evaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
    
    var userText = "What is the capital of France?";
    var systemText = "You are a geography expert.";
    var responseContent = "The capital of France is Paris.";
    
    EvaluationRequest evaluationRequest = new EvaluationRequest(userText, systemText, responseContent);
    EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);
    
    assertThat(evaluationResponse.isPass()).isTrue();
}
```

**Fact-Checking Evaluation** [`FactCheckingEvaluatorTest.java`](src/test/java/dev/danvega/workshop/evals/FactCheckingEvaluatorTest.java):

```java
@Test
void shouldDetectFactualInaccuracies() {
    var evaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));
    
    var supportingData = List.of("The Earth orbits around the Sun.");
    var factuallyIncorrectResponse = "The Sun orbits around the Earth.";
    
    EvaluationRequest request = new EvaluationRequest("What orbits what in our solar system?", supportingData, factuallyIncorrectResponse);
    EvaluationResponse response = evaluator.evaluate(request);
    
    assertThat(response.isPass()).isFalse();
}
```

**Structured Output Testing** [`StructuredOutputTest.java`](src/test/java/dev/danvega/workshop/evals/StructuredOutputTest.java):

```java
@Test
void shouldGenerateValidVacationItinerary() {
    Itinerary itinerary = chatClient.prompt()
            .user("Create a 3-day vacation itinerary for Tokyo")
            .call()
            .entity(Itinerary.class);
            
    assertThat(itinerary.itinerary()).hasSize(3);
    assertThat(itinerary.itinerary().get(0).activity()).isNotBlank();
}
```

## 🔧 Testing with Testcontainers

The project uses Testcontainers for integration testing with local models:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>ollama</artifactId>
    <scope>test</scope>
</dependency>
```

**Running Tests:**

```bash
# Ensure Docker is running
docker --version

# Run all tests
./mvnw test

# Run specific evaluation tests
./mvnw test -Dtest=SentimentAnalysisTest
./mvnw test -Dtest=RelevancyEvaluatorTest
./mvnw test -Dtest=FactCheckingEvaluatorTest
```

## 📖 HTTP Client Examples

Test the endpoints using your preferred HTTP client:

**Basic Chat:**
```bash
curl "http://localhost:8080/?message=Tell me a joke about Java"
```

**Streaming Response:**
```bash
curl "http://localhost:8080/stream"
```

**Structured Output:**
```bash
curl "http://localhost:8080/vacation/structured"
```

**RAG Query:**
```bash
curl "http://localhost:8080/rag/models?message=What are the top 3 LLMs by context window size?"
```

**Weather Tool:**
```bash
curl "http://localhost:8080/weather?city=Atlanta"
```

## 💡 Perfect For

- Java developers entering the AI space
- Spring Framework users wanting AI capabilities  
- Developers building chatbots and intelligent features
- Anyone seeking practical AI implementation without ML theory

## 🔗 Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Documentation](https://platform.openai.com/docs/introduction)
- [Ollama Documentation](https://ollama.ai/)
- [Course Resources](RESOURCES.md)
- [Dan Vega's Newsletter](https://www.danvega.dev/newsletter)

## 🤝 Connect & Learn More

- **Website**: [danvega.dev](https://www.danvega.dev)
- **Twitter**: [@therealdanvega](https://twitter.com/therealdanvega)  
- **GitHub**: [danvega](https://github.com/danvega)
- **LinkedIn**: [danvega](https://www.linkedin.com/in/danvega)

## 🚀 Ready to Transform Your Development Career?

This comprehensive workshop provides everything you need to build intelligent applications that users love and businesses need. Start building AI applications today and transform your Java development skills for the AI era!

**Happy coding! 🎉**