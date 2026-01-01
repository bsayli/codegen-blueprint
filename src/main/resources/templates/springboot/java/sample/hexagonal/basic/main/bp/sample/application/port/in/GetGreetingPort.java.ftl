package ${projectPackageName}.bp.sample.application.port.in;

import ${projectPackageName}.bp.sample.application.port.in.model.GetGreetingQuery;
import ${projectPackageName}.bp.sample.application.port.in.model.GetGreetingResult;

/**
 * Application-level use case for retrieving greetings.
 * Kept small and focused on intent:
 *  - default greeting
 *  - personalized greeting
 */
public interface GetGreetingPort {

    GetGreetingResult getDefault();

    GetGreetingResult getPersonal(GetGreetingQuery query);
}