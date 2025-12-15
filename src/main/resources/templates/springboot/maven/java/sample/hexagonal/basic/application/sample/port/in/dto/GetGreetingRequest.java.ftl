package ${projectPackageName}.application.sample.port.in.dto;

/**
* Input model for the GetGreeting use case.
* Intentionally tiny:
*  - shows how even simple use cases use a dedicated input type
*  - can evolve (e.g. locale, channel, tenant) without breaking the interface
*/
public record GetGreetingRequest(String name) {

public static GetGreetingRequest anonymous() {
return new GetGreetingRequest(null);
}
}