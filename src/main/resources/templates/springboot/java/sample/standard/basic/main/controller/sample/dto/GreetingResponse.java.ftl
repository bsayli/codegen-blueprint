package ${projectPackageName}.controller.sample.dto;

/**
 * REST response DTO for the Greeting sample.
 * Intent:
 * - keep transport shape stable and simple
 * - represent id as a String (UUID.toString)
 */
public record GreetingResponse(String id, String text) {}