package ${projectPackageName}.repository.sample;

import ${projectPackageName}.domain.sample.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple audit implementation for BASIC sample: log the created greeting.
 * Intent:
 * - demonstrate an outbound side effect in layered style
 * - keep it minimal and easy to replace later
 */
public class LoggingGreetingAuditRepository implements GreetingAuditRepository {

    private static final Logger log = LoggerFactory.getLogger(LoggingGreetingAuditRepository.class);

    @Override
    public void auditCreated(Greeting greeting) {
        if (greeting == null) {
            log.warn("GreetingAuditRepository.auditCreated invoked with null Greeting");
            return;
        }

        log.info("Greeting created: id={}, text={}", greeting.id(), greeting.text().value());
    }
}