package ${projectPackageName}.bp.sample.application.usecase;

import ${projectPackageName}.bp.sample.application.port.in.GetGreetingPort;
import ${projectPackageName}.bp.sample.application.port.in.model.GetGreetingQuery;
import ${projectPackageName}.bp.sample.application.port.in.model.GetGreetingResult;
import ${projectPackageName}.bp.sample.domain.model.Greeting;
import ${projectPackageName}.bp.sample.domain.port.out.GreetingAuditPort;
import ${projectPackageName}.bp.sample.domain.service.GreetingService;

/**
 * Application-layer handler for the GetGreeting use case.
 * Responsibilities:
 *  - orchestrate domain services
 *  - call outbound ports (e.g., audit)
 *  - map domain objects to use case result DTOs
 */
public class GetGreetingHandler implements GetGreetingPort {

    private final GreetingService greetingService;
    private final GreetingAuditPort auditPort;

    public GetGreetingHandler(
            GreetingService greetingService,
            GreetingAuditPort auditPort) {
        this.greetingService = greetingService;
        this.auditPort = auditPort;
    }

    @Override
    public GetGreetingResult getDefault() {
        Greeting greeting = greetingService.defaultGreeting();
        auditPort.auditCreated(greeting);
        return map(greeting);
    }

    @Override
    public GetGreetingResult getPersonal(GetGreetingQuery query) {
        Greeting greeting;
        if (query == null || query.name() == null) {
            greeting = greetingService.defaultGreeting();
        } else {
            greeting = greetingService.createPersonalGreeting(query.name());
        }
        auditPort.auditCreated(greeting);
        return map(greeting);
    }

    private GetGreetingResult map(Greeting greeting) {
        return GetGreetingResult.of(
                greeting.id().value(),
                greeting.text().value()
        );
    }
}