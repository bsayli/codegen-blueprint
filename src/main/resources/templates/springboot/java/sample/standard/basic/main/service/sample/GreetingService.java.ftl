package ${projectPackageName}.service.sample;

import ${projectPackageName}.domain.sample.model.Greeting;
import ${projectPackageName}.domain.sample.service.GreetingDomainService;
import ${projectPackageName}.repository.sample.GreetingAuditRepository;

/**
 * Application/use-case service for layered BASIC sample.
 * Intent:
 * - orchestrate domain service calls
 * - trigger audit side effect (no persistence)
 * - return domain objects; controller decides the response shape
 */
public class GreetingService {

    private final GreetingDomainService domainService;
    private final GreetingAuditRepository auditRepository;

    public GreetingService(GreetingDomainService domainService, GreetingAuditRepository auditRepository) {
        this.domainService = domainService;
        this.auditRepository = auditRepository;
    }

    public Greeting getDefaultGreeting() {
        Greeting greeting = domainService.defaultGreeting();
        auditRepository.auditCreated(greeting);
        return greeting;
    }

    public Greeting getPersonalGreeting(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getDefaultGreeting();
        }
        Greeting greeting = domainService.createPersonalGreeting(name);
        auditRepository.auditCreated(greeting);
        return greeting;
    }
}