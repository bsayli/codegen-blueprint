package ${projectPackageName}.bp.sample.domain.model;

import ${projectPackageName}.bp.sample.domain.model.value.GreetingId;
import ${projectPackageName}.bp.sample.domain.model.value.GreetingText;

/**
 * Aggregate root for the Greeting sample (Layered).
 * Intent:
 * - keep domain data and small behaviors together
 * - keep invariants inside value objects
 * - keep this class framework-free and persistence-agnostic
 */
public class Greeting {

    private final GreetingId id;
    private final GreetingText text;

    private Greeting(GreetingId id, GreetingText text) {
        this.id = id;
        this.text = text;
    }

    public static Greeting of(GreetingId id, GreetingText text) {
        return new Greeting(id, text);
    }

    public static Greeting createDefault() {
        return new Greeting(GreetingId.newId(), GreetingText.of("Hello from layered sample!"));
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