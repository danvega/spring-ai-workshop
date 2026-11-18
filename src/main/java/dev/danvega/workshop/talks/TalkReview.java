package dev.danvega.workshop.talks;

import java.util.List;

public record TalkReview(int score, List<String> strengths, List<String> improvements, String betterTitle) {
}
