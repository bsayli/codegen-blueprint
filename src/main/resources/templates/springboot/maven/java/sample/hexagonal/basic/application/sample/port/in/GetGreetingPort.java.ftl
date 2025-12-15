package ${projectPackageName}.application.sample.port.in;

import ${projectPackageName}.application.sample.port.in.dto.GetGreetingRequest;
import ${projectPackageName}.application.sample.port.in.dto.GetGreetingResult;

/**
* Application-level use case for retrieving greetings.
* Kept small and focused on intent:
*  - default greeting
*  - personalized greeting
*/
public interface GetGreetingPort {

GetGreetingResult getDefault();

GetGreetingResult getPersonal(GetGreetingRequest request);
}
