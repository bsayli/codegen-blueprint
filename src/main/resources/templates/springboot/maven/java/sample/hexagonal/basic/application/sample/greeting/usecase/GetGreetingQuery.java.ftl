package ${projectPackageName}.application.sample.greeting.usecase;

/**
 * Input model for the GetGreeting use case.
 * Intentionally tiny:
 *  - shows how even simple use cases use a dedicated input type
 *  - can evolve (e.g. locale, channel, tenant) without breaking the interface
 */
public record GetGreetingQuery(String name) {

    public static GetGreetingQuery anonymous() {
        return new GetGreetingQuery(null);
    }
}