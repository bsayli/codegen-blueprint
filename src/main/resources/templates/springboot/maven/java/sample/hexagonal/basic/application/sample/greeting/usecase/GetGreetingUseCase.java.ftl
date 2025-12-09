package ${projectPackageName}.application.sample.greeting.usecase;

/**
 * Application-level use case for retrieving greetings.
 * Kept small and focused on intent:
 *  - default greeting
 *  - personalized greeting
 */
public interface GetGreetingUseCase {

    GetGreetingResult getDefault();

    GetGreetingResult getPersonal(GetGreetingQuery query);
}