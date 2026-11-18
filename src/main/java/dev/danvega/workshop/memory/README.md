# Memory

A model remembers nothing between calls. Each call, it only knows what is in the prompt. So memory is your code putting text back into the prompt.

There are two kinds:

| | Short-term memory | Long-term memory |
|---|---|---|
| **Keyed by** | Conversation | User |
| **Stores** | Every message, word for word | Short facts, written by the model |
| **Comes back as** | The last N messages | Only the facts that matter |
| **Decided by** | Your code | The model, through tools |

Short-term memory is a transcript. Long-term memory is notes. Storing short-term memory in a database does not make it long-term: it is still keyed by conversation, so a new chat starts empty.

## Start here

[StatefulController.java](StatefulController.java) is the smallest version: `GET /memory?message=...` with an optional `conversation` parameter.

## Step 1: short-term memory

[ShortTermMemoryController.java](ShortTermMemoryController.java) and [MemoryConfig.java](MemoryConfig.java). Short-term memory has three parts:

- **When:** `MessageChatMemoryAdvisor` loads the history before each call and saves it after.
- **What:** `MessageWindowChatMemory` keeps the last 10 messages (the default is 20).
- **Where:** `ChatMemoryRepository` stores them. By default that is `InMemoryChatMemoryRepository`, a `Map` in the JVM, so a restart wipes it.

Add a starter like `spring-ai-starter-model-chat-memory-repository-jdbc` and the JDBC repository replaces the in-memory one. No code changes. JDBC, MongoDB, Redis, Cassandra and Neo4j are supported.

Try it with S1 to S5 in [workshop.http](../../../../../../../workshop.http). It remembers within a conversation. `GET /memory/short-term/s1` shows what the repository holds. A new conversation ID starts empty.

## Step 2: long-term memory

[LongTermMemoryController.java](LongTermMemoryController.java). The model gets six tools from `AutoMemoryTools` (Spring AI Community, `spring-ai-agent-utils`) and keeps Markdown files per user: `memories/maria/MEMORY.md` (the index) plus one file per topic, like `travel.md`.

The two lines that matter:

```java
// Short-term: this chat
.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId + ":" + request.conversationId()))
// Long-term: this user's folder
.tools(AutoMemoryTools.builder().memoriesDir(root.resolve(userId)).build())
```

Our code picks the folder, never the model. User IDs can only hold letters, numbers and dashes, and the tools refuse any path outside the folder. `DELETE /memory/long-term/maria` forgets a user: their folder and every conversation.

Try it with L1 to L5 in [workshop.http](../../../../../../../workshop.http). Keep the `memories/` folder open and watch the files appear. Set `workshop.memory.root` to keep the files somewhere else.

## Good to know

- **The model decides when to save and when to read.** A small model with no reasoning may skip the tools. If recall must be reliable, have your code load the facts into the prompt.
- **A conversation ID is required in Spring AI 2.x.** There is no default conversation any more.
- **Memory saves the final exchange, not the tool loop.** The memory advisor runs outside `ToolCallingAdvisor`.
- **Core Spring AI's long-term option** is `VectorStoreChatMemoryAdvisor`. It saves every message to a vector store and adds the closest ones to each prompt, with no model decisions.
- **User facts in plain files** raise real questions: encryption, retention, and deleting on request.
