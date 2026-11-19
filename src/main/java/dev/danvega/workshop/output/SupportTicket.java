package dev.danvega.workshop.output;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record SupportTicket(
        String customer,
        Priority priority,
        Category category,
        @JsonPropertyDescription("One sentence, under 20 words") String summary,
        boolean refundRequested) {

    public enum Priority { LOW, MEDIUM, HIGH, URGENT }

    public enum Category { BILLING, BUG, ACCOUNT, OTHER }
}
