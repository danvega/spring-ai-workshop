# Spring AI Workshop

This is a repository that contains all the code for my Spring AI Workshop. In this workshop we go through an introduction to AI and building intelligent applications in Java with Spring AI.

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
  - Prompt
  - Output
  - RAG

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

## Large Language Models (LLMs)

In this section of the workshop you will learn about how to consume Large Language Models (LLMs).

### Models

- [Open AI](https://openai.com/)
- [Google Gemini](https://ai.google.dev/gemini-api)
- [Hugging Face](https://huggingface.co/)

### Tokens 

Tokens are the currency of LLMs - OpenAI's large language models (sometimes referred to as GPT's) process text using tokens, which are common sequences of characters found in a set of text.

- https://openai.com/api/pricing/
- https://platform.openai.com/tokenizer

### AI REST Endpoints

To be able to call the Open AI REST endpoint you need to signup for an [Open AI API](https://platform.openai.com/). Once you have an API key you can call a REST endpoint using cURL or Java.

```shell
#!/bin/bash
echo "Calling Open AI..."
MY_OPENAI_KEY="YOUR_API_KEY_HERE"
PROMPT="Tell me a dad joke about banks"

curl https://api.openai.com/v1/chat/completions \
-H "Content-Type: application/json" \
-H "Authorization: Bearer $MY_OPENAI_KEY" \
-d '{ "model": "gpt-4o", "messages": [{"role":"user", "content": "'"${PROMPT}"'"}] }'
```

```java
public class HelloOpenAI {

    public void call() throws IOException, InterruptedException {
        var apiKey = "YOUR_API_KEY";
        var body = """
                {
                    "model": "gpt-4o",
                    "messages": [
                        {
                            "role": "user",
                            "content": "Tell me a good dad joke about Dogs"
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

}
```

## Spring AI

The Spring AI project aims to streamline the development of applications that incorporate artificial intelligence functionality without unnecessary complexity.

The project draws inspiration from notable Python projects, such as LangChain and LlamaIndex, but Spring AI is not a direct port of those projects. The project was founded with the belief that the next wave of Generative AI applications will not be only for Python developers but will be ubiquitous across many programming languages.

### Documentation / Getting Started 

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring Initializr](https://start.spring.io)
- Web,Open AI, DevTools
- application.properties
  - Hardcoding string
  - Environment Variables

```properties
spring.application.name=ai-workshop
spring.ai.openai.api-key=YOUR_OPEN_AI_API_KEY
spring.ai.openai.chat.options.model=gpt-4o
```

**HTTP Clients**

You will be creating a number of REST endpoints that call OpenAI's GPT-4o Model. To manually test the endpoints you will need an HTTP client. There are a number of options below, but I will be using httpie. 

- [IntelliJ](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html) 
- [Postman](https://www.postman.com/)
- [cURL](https://curl.se/)
- [httpie](https://httpie.io/)

### Chat Client 

The ChatClient offers a fluent API for communicating with an AI Model. It supports both a synchronous and reactive programming model.

The `ChatController` demo shows off how to get an instance of the ChatClient and how to call the LLM using the fluent API. 

```java
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about Banks") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content(); // short for getResult().getOutput().getContent();
    }
    
    @GetMapping("/jokes-by-topic")
    public String jokesByTopic(@RequestParam String topic) {
        return chatClient.prompt()
                .user(u -> u.text("Tell me a joke about {topic}").param("topic",topic))
                .call()
                .content();
    }
    
    @GetMapping("jokes-with-response")
    public ChatResponse jokeWithResponse(@RequestParam(value = "message", defaultValue = "Tell me a dad joke about computers") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

}
```

The `StreamController` shows off an example of using the `stream()` method. The stream lets you get an asynchronous response

```java
@RestController
public class StreamController {

  private final ChatClient chatClient;

  public StreamController(ChatClient.Builder builder) {
    this.chatClient = builder
            .build();
  }

  @GetMapping("/without-stream")
  public String withoutStream(@RequestParam(
          value = "message",
          defaultValue = "I'm visiting Toronto this week, what are 10 places I must visit?") String message) {

    return chatClient.prompt()
            .user(message)
            .call()
            .content();
  }

  // http --stream :8080/stream
  @GetMapping("/stream")
  public Flux<String> stream(@RequestParam(
          value = "message",
          defaultValue = "I'm visiting Toronto this week, what are 10 places I must visit?") String message) {
    return chatClient.prompt()
            .user(message)
            .stream()
            .content();
  }

}
```

### Chat Memory

The web is stateless, and we need to remember that when working the various REST endpoints that the LLMs provide. This might be a bit confusing because you have probably used ChatGPT before, and it remembers previous conversations and can build upon them. You have to remember that ChatGPT is a product that talks to an LLM like GPT-4o and the product is what preserves conversational history. 

The interface `ChatMemory` represents a storage for chat conversation history. It provides methods to add messages to a * conversation, retrieve messages from a conversation, and clear the conversation history. There is one implementation InMemoryChatMemory that provides in-memory storage for chat conversation history.

In the following example you can use the in memory chat memory to send previous conversations along as context. 

```java
@RestController
public class StatefulController {

    private final ChatClient chatClient;

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

}
```

Run the previous example and let the LLM know what your name is and then send a follow-up request asking the LLM if it remembers your name. Try running this example by commenting out the `defaultAdvisors()` line of code. 

```shell
http :8080/chat message=="My name is Dan, how are you doing today?"
```

```shell
http :8080/chat message=="What is my name?"
```

### Prompts

Prompts serve as the foundation for the language-based inputs that guide an AI model to produce specific outputs. For those familiar with ChatGPT, a prompt might seem like merely the text entered into a dialog box that is sent to the API. However, it encompasses much more than that. In many AI Models, the text for the prompt is not just a simple string.

You might have heard the term "Prompt Engineering" which I'm not a very big fan of but the idea behind it is. Really what we are talking about here is learning how to effectively communicate with an AI model. It is such an import part building AI powered applications and getting your desired output from an LLM.

- [Prompt Engineering Guidelines from Open AI](https://platform.openai.com/docs/guides/prompt-engineering)
- [Deep Learning: GhatGPT Prompt Engineering for Developers](https://www.deeplearning.ai/short-courses/chatgpt-prompt-engineering-for-developers/)

```java
@RestController
public class TreasureController {

    private final ChatClient chatClient;
    
    public TreasureController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Please respond to any question in the voice of a pirate.")
                .build();
    }
    
    @GetMapping("/treasure")
    public String treasureFacts() {
        return chatClient.prompt()
                .user("Tell me a really interesting fact about famous pirate treasures. Please keep your answer to 1 or 2 sentences.")
                .call()
                .content();
    }
}
```

```java
@RestController
@RequestMapping("/youtube")
public class YouTube {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/youtube.st")
    private Resource ytPromptResource;

    public YouTube(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/popular")
    public String findPopularYouTubersStepOne(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        String message = """
            List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
            the answer , just say "I don't know".
            """;

        return chatClient.prompt()
                .user(u -> u.text(message).param("genre",genre))
                .call()
                .content();
    }

    @GetMapping("/popular-resource")
    public String findPopularYouTubers(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        return chatClient.prompt()
                .user(u -> u.text(ytPromptResource).param("genre",genre))
                .call()
                .content();
    }

}
```

### Structured Output 

The ability of LLMs to produce structured outputs is important for downstream applications that rely on reliably parsing output values. Developers want to quickly turn results from an AI model into data types, such as JSON, XML or Java Classes, that can be passed to other application functions and methods.

https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html

```java
public record ActorFilms(String actor, List<String> movies) {}
```

```java
@RestController
public class ActorController {

    private final ChatClient chatClient;

    public ActorController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/films")
    public ActorFilms getActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for a Anthony Hopkins.")
                .call()
                .entity(ActorFilms.class);
    }

    @GetMapping("/films-list")
    public List<ActorFilms> listActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for the actors Denzel Washington, Leonardo DiCaprio and Tom Hanks")
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

}
```

### Bring your own data

How can you equip the AI model with information on which it has not been trained? Note that the GPT 3.5/4.0 dataset extends only until September 2021. Consequently, the model says that it does not know the answer to questions that require knowledge beyond that date. 

There is also the point that an LLM is not trained on your company data that it does not have access to. Three techniques exist for customizing the AI model to incorporate your data:

**Fine Tuning**: This traditional machine learning technique involves tailoring the model and changing its internal weighting. However, it is a challenging process for machine learning experts and extremely resource-intensive for models like GPT due to their size. Additionally, some models might not offer this option.

**Prompt Stuffing**: A more practical alternative involves embedding your data within the prompt provided to the model. Given a model’s token limits, techniques are required to present relevant data within the model’s context window. This approach is colloquially referred to as “stuffing the prompt.” The Spring AI library helps you implement solutions based on the “stuffing the prompt” technique otherwise known as Retrieval Augmented Generation (RAG).

**Function Calling**: This technique allows registering custom, user functions that connect the large language models to the APIs of external systems. Spring AI greatly simplifies code you need to write to support function calling.

#### Retrieval Augmented Generation (RAG)

A technique termed Retrieval Augmented Generation (RAG) has emerged to address the challenge of incorporating relevant data into prompts for accurate AI model responses. The approach involves a batch processing style programming model, where the job reads unstructured data from your documents, transforms it, and then writes it into a vector database. At a high level, this is an ETL (Extract, Transform and Load) pipeline. The vector database is used in the retrieval part of RAG technique.

**Vector Stores**

A vector databases is a specialized type of database that plays an essential role in AI applications. In vector databases, queries differ from traditional relational databases. Instead of exact matches, they perform similarity searches. When given a vector as a query, a vector database returns vectors that are “similar” to the query vector. 

Spring AI provides support for a number of Vector Databases through Auto-Configuration. Here are a few of the supported vector databases if you want to see a full list please go [here](https://docs.spring.io/spring-ai/reference/api/vectordbs.html#_available_implementations). 

- PgVector Store
- Azure Vector Search
- Oracle Vector Store
- Redis Vector Store
- SimpleVectorStore

**Embeddings**

Embeddings transform text into numerical arrays or vectors, enabling AI models to process and interpret language data. This transformation from text to numbers is a key element in how AI interacts with and understands human language.

![Embeddings](/images/embeddings.png)

#### Bring you own data Demos

- **Stuffing the Prompt**: `stuff/Olympics.java`
- **RAG**: `/rag/FaqController.java`
- **Functions**: `/functions/CityController.java`

### Multimodal API

Humans process knowledge, simultaneously across multiple modes of data inputs. The way we learn, our experiences are all multimodal. We don’t have just vision, just audio and just text. In the previous examples we took in some text and generated some text. In this example you will use an image as the input and generate some text as the output. 

```java
@RestController
public class ImageDetection {

    private final ChatClient chatClient;
    @Value("classpath:/images/sincerely-media-2UlZpdNzn2w-unsplash.jpg")
    Resource sampleImage;

    public ImageDetection(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/image-to-text")
    public String image() throws IOException {
        return chatClient.prompt()
                .user(u -> u
                        .text("Can you please explain what you see in the following image?")
                        .media(MimeTypeUtils.IMAGE_JPEG,sampleImage)
                )
                .call()
                .content();
    }
}
```