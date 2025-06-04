# Spring AI Evaluation Examples

This package demonstrates different techniques for evaluating AI model responses using the Spring AI framework. Learn how to assess quality, relevance, and accuracy of AI-generated content through practical examples.

## Prerequisites

- **Java** - JDK 17 or later
- **Maven or Gradle** - For building and running tests
- **Docker Desktop** - Required for local LLM instances
- **Ollama** - Install and pull required models:
  ```bash
  ollama pull bespokeai/minicheck
  ```

## Test Examples

### 1. Sentiment Analysis Test

**What it does:** Evaluates an AI model's ability to classify text sentiment (POSITIVE, NEGATIVE, NEUTRAL).

**Key features:**
- Uses Spring AI's `ChatClient` for LLM interaction
- Maps responses to Java enums with `.entity(Sentiment.class)`
- Low temperature setting ensures consistent results
- Standard JUnit assertions validate expected outcomes

**Running the test:**
```bash
mvn test -Dtest=SentimentAnalysisTest
```

### 2. Relevancy Evaluator Test

**What it does:** Determines if AI responses are relevant to user queries and provided context - perfect for RAG system evaluation.

**Key features:**
- Uses Spring AI's `RelevancyEvaluator`
- Automatically provisions Mistral model via Testcontainers
- Evaluates response relevance with `isPass()` method
- No manual Docker setup required

**Running the test:**
```bash
mvn test -Dtest=RelevancyEvaluatorDemoTest
```

### 3. Fact Checking Evaluator Test

**What it does:** Verifies factual accuracy of AI claims against reference documents to detect hallucinations.

**Key features:**
- Uses Spring AI's `FactCheckingEvaluator`
- Leverages specialized `bespokeai/minicheck` model
- Validates claims against provided documentation
- Returns pass/fail status for factual consistency

**Running the test:**
```bash
mvn test -Dtest=FactCheckingEvaluatorDemoTest
```

### 4. Structured Output Test

**What it does:** Validates that AI responses can be automatically mapped to Java objects with the expected structure and content.

**Key features:**
- Tests Spring AI's `.entity()` method for type-safe response mapping
- Generates vacation itineraries that map to custom Java records
- Validates that structured responses contain required fields
- Ensures AI output conforms to predefined data models

**Running the test:**
```bash
mvn test -Dtest=StructuredOutputTest
```

## Running All Tests

Execute all tests with a single command:

```bash
# Maven
mvn test

# Gradle
./gradlew test
```

**Note:** Ensure Docker Desktop is running before executing tests that use Testcontainers.

## Technology Stack

- **Spring AI** - Core framework for AI integration
- **JUnit 5** - Testing framework
- **Testcontainers** - Automated container management
- **Ollama** - Local LLM runtime

## Customization Options

Adapt these examples to your needs by customizing:

- **Evaluation prompts** - Tailor criteria for your use case
- **LLM models** - Switch between different models for generation or evaluation
- **Chat options** - Adjust temperature, token limits, and other parameters
- **Evaluation thresholds** - Define custom pass/fail criteria

See the [Spring AI documentation](https://docs.spring.io/spring-ai/reference/) for detailed customization guides.

## Getting Started

1. Clone the repository
2. Install prerequisites
3. Pull required Ollama models
4. Run individual tests or the full suite
5. Explore the code to understand evaluation patterns

## Contributing

Feel free to extend these examples with additional evaluation techniques or improvements to existing tests.