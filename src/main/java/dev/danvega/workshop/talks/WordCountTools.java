package dev.danvega.workshop.talks;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

// Models are bad at counting words. This tool gives an exact answer.
public class WordCountTools {

    @Tool(description = "Count the words in a piece of text, like a talk title or description")
    public int countWords(@ToolParam(description = "The text to count") String text) {
        return text.isBlank() ? 0 : text.trim().split("\\s+").length;
    }
}
