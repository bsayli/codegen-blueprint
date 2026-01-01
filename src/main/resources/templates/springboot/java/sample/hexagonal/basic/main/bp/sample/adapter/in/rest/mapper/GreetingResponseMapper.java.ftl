package ${projectPackageName}.bp.sample.adapter.in.rest.mapper;

import ${projectPackageName}.bp.sample.adapter.in.rest.dto.GreetingResponse;
import ${projectPackageName}.bp.sample.application.port.in.model.GetGreetingResult;

/**
 * Maps application-layer result to REST response DTO.
 * GetGreetingResult → GreetingResponse
 * Kept free of Spring annotations to stay framework-agnostic.
 * It will be wired via a bootstrap @Configuration class.
 */
public class GreetingResponseMapper {

    public GreetingResponse from(GetGreetingResult result) {
        if (result == null) {
            return null;
        }
        return new GreetingResponse(
                result.id() != null ? result.id().toString() : null,
                result.text()
        );
    }
}