package ${projectPackageName}.application.sample.greeting.usecase;

import ${projectPackageName}.domain.sample.greeting.model.Greeting;
import ${projectPackageName}.domain.sample.greeting.port.out.GreetingAuditPort;
import ${projectPackageName}.domain.sample.greeting.service.GreetingService;

/**
 * Application-layer handler for the GetGreeting use case.
 * Responsibilities:
 *  - orchestrate domain services
 *  - call outbound ports (e.g. audit)
 *  - map domain objects to use case result DTOs
 */
public class GetGreetingHandler implements GetGreetingUseCase {

    private final GreetingService greetingService;
    private final GreetingAuditPort auditPort;

    public GetGreetingHandler(GreetingService greetingService,
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