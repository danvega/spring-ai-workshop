# Model Router

Send each prompt to the cheapest model that can handle it. Easy questions go to a small model. Hard ones go to a big model.

This demo uses the [Jev Spring Boot starter](https://github.com/danvega/jev-spring-boot-starter) to make the routing decision. Spring AI then answers with the model Jev picked.

## How it works

1. `RouterProperties` holds the model list under `router.*` in `application.properties`. Each model has an id, a tier (economy, balanced, premium), and a description of what it is good at.
2. `ModelRouter` sends the prompt and the model list to Jev as a single choice question. Jev returns one model id with a confidence score.
3. `RouterController` answers the prompt with the chosen model. It passes the model id as a per-request option on the `ChatClient`.

If Jev is unavailable, returns an unknown model, or is not confident enough (`router.confidence-threshold`), the `router.fallback` model answers instead. The response says which model answered and why.

## Setup

Two environment variables are required. The Jev starter reads `TYPESAFE_API_KEY` automatically.

```bash
export OPENAI_API_KEY='your-openai-key'
export TYPESAFE_API_KEY='your-typesafe-key'
```

The starter is a local snapshot. If it is missing from your local Maven repository, install it first:

```bash
cd ~/dev/jev/jev-spring-boot-starter && ./mvnw install
```

## Try it

An easy question should route to the economy tier:

```bash
curl "localhost:8080/router?message=What%20is%20the%20capital%20of%20Ohio"
```

A hard one should route to premium:

```bash
curl "localhost:8080/router?message=Design%20a%20rate%20limiter%20for%20a%20multi-region%20API%20and%20explain%20the%20tradeoffs"
```

The JSON response includes the chosen model, its tier, Jev's confidence, whether the fallback was used, and the answer.
