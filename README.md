# Spring AI Workshop

Hands-on demos for building AI applications with Spring Boot and Spring AI. Each package under `dev.danvega.workshop` shows one feature: chat, prompts, structured output, memory, advisors, tools, RAG, guardrails, multimodal, evals, and model routing. Every demo is a small REST endpoint you can hit from a browser, curl, or the included `workshop.http` file.

Tested with Java 26, Spring Boot 4.1.2, and Spring AI 2.0.2.

## What you need

- JDK 26
- An OpenAI API key
- A TypeSafe API key for the model router demo

```bash
export OPENAI_API_KEY='your-openai-key'
export TYPESAFE_API_KEY='your-typesafe-key'
```

The router demo uses the [Jev Spring Boot starter](https://github.com/danvega/jev-spring-boot-starter), a local snapshot. Install it once:

```bash
cd ../jev-spring-boot-starter && ./mvnw install
```

## Run it

```bash
./mvnw spring-boot:run
```

The app starts on http://localhost:8080.

## Try it

Open [workshop.http](workshop.http) in IntelliJ or VS Code and run requests from there, or start with:

```bash
curl "localhost:8080/?message=Tell me a dad joke about dogs"
curl "localhost:8080/vacation/structured"
curl "localhost:8080/router?message=What is the capital of Ohio"
```

## The demos

| Package | Shows | Endpoints |
| --- | --- | --- |
| `chat` | ChatClient basics, `.content()` vs `.chatResponse()` | `/`, `/jokes-by-topic`, `/jokes-with-response` |
| `chatmodel` | ChatModel API, usage metadata, multiple generations | `/fact`, `/taglines` |
| `prompt` | Prompt templates loaded from resources | `/posts/new` |
| `output` | Structured output into Java records | `/vacation/structured`, `/vacation/unstructured` |
| `options` | Configuring model options ([README](src/main/java/dev/danvega/workshop/options/README.md)) | `/options` |
| `memory` | Chat memory and stateful conversations | `/memory` |
| `advisor` | Custom advisors: tool logging, token counting | `/advisor` |
| `async` | Streaming responses | `/stream` |
| `multimodal` | Image to text, image generation, text to speech | `/image-to-text`, `/generate-image`, `/speak` |
| `byod` | Bring your own data by stuffing the prompt | `/models`, `/models/stuff-the-prompt` |
| `rag` | Retrieval augmented generation over `data/models.json` | `/rag/models` |
| `guards` | Input validation, fact checking, PII masking advisors | `/input-validation`, `/fact-checking`, `/pii-masking` |
| `tools` | Tool calling: date and time, task actions, weather ([README](src/main/java/dev/danvega/workshop/tools/action/README.md)) | `/tools`, `/tasks`, `/weather/alerts` |
| `router` | Jev picks the cheapest capable model per prompt ([README](src/main/java/dev/danvega/workshop/router/README.md)) | `/router` |
| `evals` | Testing AI output with evaluators ([README](src/main/java/dev/danvega/workshop/evals/README.md)) | test sources |
| `observability` | Metrics for AI calls ([README](src/main/java/dev/danvega/workshop/observability/README.md)) | actuator |

There is also a minimal `SimpleChatController` at `/java` in the root package. The `agents` and `multiplellms` packages hold notes in their READMEs.

## Testing

```bash
./mvnw test
```

The eval tests call the live OpenAI API, so they need `OPENAI_API_KEY` and can flake on rare occasions.

## Good to know

- This is demo code for a workshop, not production code.
- Configuration lives in [application.yml](src/main/resources/application.yml). The default chat model is set there.
- Slides for the talk are in the [slides](slides) folder.
