package ${projectPackageName}.bp.sample.controller.mapper;

import ${projectPackageName}.bp.sample.controller.dto.GreetingResponse;
import ${projectPackageName}.bp.sample.domain.model.Greeting;

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