package ${projectPackageName}.domain.sample.model;

import ${projectPackageName}.domain.sample.model.value.GreetingId;
import ${projectPackageName}.domain.sample.model.value.GreetingText;

/**
 * Aggregate root for the Greeting sample.
 * Intent:
 * - show a small, self-contained aggregate
 * - keep invariants inside value objects
 * - keep this class as a simple composition of those values
 */
public class Greeting {

    private final GreetingId id;
    private final GreetingText text;

    private Greeting(GreetingId id, GreetingText text) {
        this.id = id;
        this.text = text;
    }

    public static Greeting of(GreetingId id, GreetingText text) {
        // Invariants are already enforced in value objects
        return new Greeting(id, text);
    }

    public static Greeting createDefault() {
        return new Greeting(
                GreetingId.newId(),
                GreetingText.of("Hello from hexagonal sample!")
        );
    }

    public GreetingId id() {
        return id;
    }

    public GreetingText text() {
        return text;
    }

    public String render() {
        return text.value();
    }
}