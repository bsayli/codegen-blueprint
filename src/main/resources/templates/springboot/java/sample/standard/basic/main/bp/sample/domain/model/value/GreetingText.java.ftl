package ${projectPackageName}.bp.sample.domain.model.value;

import java.util.Objects;

/**
 * Value Object capturing greeting text with basic invariants.
 * Intent:
 * - trim input
 * - enforce not-blank
 * - enforce a small max length
 */
public record GreetingText(String value) {

    private static final int MAX_LENGTH = 140;

    public GreetingText {
        Objects.requireNonNull(value, "value must not be null");

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Greeting text must not be blank");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Greeting text must not exceed " + MAX_LENGTH + " characters");
        }
        value = trimmed;
    }

    public static GreetingText of(String raw) {
        return new GreetingText(raw);
    }
}