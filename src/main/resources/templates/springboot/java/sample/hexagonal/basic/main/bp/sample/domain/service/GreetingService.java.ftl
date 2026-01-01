package ${projectPackageName}.bp.sample.domain.service;

import ${projectPackageName}.bp.sample.domain.model.Greeting;
import ${projectPackageName}.bp.sample.domain.model.value.GreetingId;
import ${projectPackageName}.bp.sample.domain.model.value.GreetingText;

/**
 * Domain service for the Greeting aggregate.
 * Intent:
 * - keep aggregate + value objects small and focused
 * - provide simple creation/use flows that can evolve over time
 */
public class GreetingService {

    /**
     * Returns a default greeting using the aggregate factory.
     */
    public Greeting defaultGreeting() {
        return Greeting.createDefault();
    }

    /**
     * Creates a Greeting from a raw text value.
     * All invariants are enforced by {@link GreetingText}.
     */
    public Greeting createGreeting(String rawText) {
        return Greeting.of(
                GreetingId.newId(),
                GreetingText.of(rawText)
        );
    }

    /**
     * Creates a simple "Hello, <name>!" style greeting.
     * Shows a tiny bit of orchestration logic in the domain layer.
     */
    public Greeting createPersonalGreeting(String name) {
        String safeName = (name == null) ? "there" : name.trim();
        if (safeName.isEmpty()) {
            safeName = "there";
        }

        return Greeting.of(
                GreetingId.newId(),
                GreetingText.of("Hello, " + safeName + "!")
        );
    }
}