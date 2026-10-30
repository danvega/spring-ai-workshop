# AGENTS.md

Guidance for AI coding agents working in this repository.

## What this is

A Spring AI workshop repo used in live talks and presentations. It is a collection of small, self-contained demos, not a production app. Optimize for code that reads well on stage. Keep demos short, obvious, and independent of each other.

## Stack

- Java 26, Spring Boot 4.1.2, Spring AI 2.0.2 (GA line, no milestones)
- Maven with the wrapper: always `./mvnw`, never a global `mvn`
- One local snapshot dependency: `dev.danvega:jev-spring-boot-starter:0.3.0-SNAPSHOT`. If it is missing, install it from `~/dev/jev/jev-spring-boot-starter` with `./mvnw install`.

## Build and test

```bash
./mvnw compile         # quick check
./mvnw test            # full suite
./mvnw spring-boot:run # app on localhost:8080
```

- Tests in `evals` call the live OpenAI API. They need `OPENAI_API_KEY` and cost a little money. They are also nondeterministic, so a rare flake is not a code bug.
- The app context needs `OPENAI_API_KEY` and `TYPESAFE_API_KEY` at startup. The Jev starter reads `TYPESAFE_API_KEY` on its own.
- No Docker and no local models. Demos that would need a big local model download are deliberately out of scope.

## Layout

One package per demo topic under `src/main/java/dev/danvega/workshop/`: `chat`, `prompt`, `output` (structured output), `memory`, `advisor`, `tools`, `rag`, `byod`, `evals`, `guards`, `multimodal`, `multiplellms`, `observability`, `options`, `router` (Jev model router), and friends. Non-obvious packages have their own `README.md` inside the package. Shared config lives in `src/main/resources/application.yml`. Demo data is in `src/main/resources/data` (`models.json` plus the generated `vectorstore.json`).

## Conventions

- Demos are plain `@RestController` classes with GET endpoints and sensible default `@RequestParam` values, so they can be hit from a browser or curl on stage.
- Build `ChatClient` from the injected `ChatClient.Builder` in the constructor.
- Spring AI 2.0 API: `ChatClient` option methods take the options builder itself, for example `.options(OpenAiChatOptions.builder().model(...))`, not a built object.
- New demo packages get a short `README.md` with what it shows, setup, and a curl example.
- Prose style for READMEs and docs: no em dashes, short sentences, plain language.

## Git

- Commit messages: short imperative subject, wrapped body explaining why.
- Do not push without being asked.
