package ${projectPackageName}.controller.sample.mapper;

import ${projectPackageName}.controller.sample.dto.GreetingResponse;
import ${projectPackageName}.domain.sample.model.Greeting;

/**
 * Maps domain object to REST response DTO.
 * Intent:
 * - isolate HTTP representation from the domain
 * - keep mapping logic out of controller
 * - remain framework-agnostic (no Spring annotations)
 */
public class GreetingResponseMapper {

    public GreetingResponse from(Greeting greeting) {
        if (greeting == null) {
            return null;
        }
        return new GreetingResponse(greeting.id().toString(), greeting.text().value());
    }
}