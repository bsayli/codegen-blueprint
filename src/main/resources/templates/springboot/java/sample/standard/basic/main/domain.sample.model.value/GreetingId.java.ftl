package ${projectPackageName}.domain.sample.model.value;

import java.util.Objects;
import java.util.UUID;

/**
 * Strongly-typed identifier for the Greeting aggregate.
 * Intent:
 * - model identity explicitly (instead of raw String/UUID everywhere)
 * - keep domain types expressive and safe
 */
public record GreetingId(UUID value) {

    public GreetingId {
        Objects.requireNonNull(value, "value must not be null");
    }

    public static GreetingId newId() {
        return new GreetingId(UUID.randomUUID());
    }

    public static GreetingId fromString(String raw) {
        return new GreetingId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}