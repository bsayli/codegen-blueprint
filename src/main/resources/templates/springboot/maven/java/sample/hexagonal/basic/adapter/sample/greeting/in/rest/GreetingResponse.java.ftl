package ${projectPackageName}.adapter.sample.greeting.in.rest;

/**
 * REST response DTO for the Greeting sample.
 * Kept transport-friendly:
 * - id is a String (UUID.toString())
 */
public record GreetingResponse(
        String id,
        String text
) {}