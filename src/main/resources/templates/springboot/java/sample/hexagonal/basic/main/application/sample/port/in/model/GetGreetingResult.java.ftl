package ${projectPackageName}.application.sample.port.in.model;

import java.util.UUID;

/**
 * Output model for the GetGreeting use case.
 * Hexagonal intent:
 *  - application returns a simple, serializable shape
 *  - adapters (REST, CLI, etc.) can map this to their own DTOs
 */
public record GetGreetingResult(UUID id, String text) {

    public static GetGreetingResult of(UUID id, String text) {
        return new GetGreetingResult(id, text);
    }
}