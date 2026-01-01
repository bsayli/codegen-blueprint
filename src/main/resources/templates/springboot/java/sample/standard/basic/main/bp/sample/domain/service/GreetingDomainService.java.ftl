package ${projectPackageName}.bp.sample.domain.service;

import ${projectPackageName}.bp.sample.domain.model.Greeting;
import ${projectPackageName}.bp.sample.domain.model.value.GreetingId;
import ${projectPackageName}.bp.sample.domain.model.value.GreetingText;

/**
 * Pure domain service for the Greeting sample (Layered).
 * Intent:
 * - provide small creation flows for the aggregate
 * - keep orchestration-free domain logic here
 * - no IO, no repositories, no Spring annotations
 */
public class GreetingDomainService {

    public Greeting defaultGreeting() {
        return Greeting.createDefault();
    }

    public Greeting createGreeting(String rawText) {
        return Greeting.of(GreetingId.newId(), GreetingText.of(rawText));
    }

    public Greeting createPersonalGreeting(String name) {
        String safeName = (name == null) ? "there" : name.trim();
        if (safeName.isEmpty()) {
            safeName = "there";
        }

        return Greeting.of(GreetingId.newId(), GreetingText.of("Hello, " + safeName + "!"));
    }
}