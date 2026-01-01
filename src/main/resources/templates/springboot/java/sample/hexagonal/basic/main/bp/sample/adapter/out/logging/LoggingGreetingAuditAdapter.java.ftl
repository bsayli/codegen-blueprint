package ${projectPackageName}.bp.sample.adapter.out.logging;

import ${projectPackageName}.bp.sample.domain.model.Greeting;
import ${projectPackageName}.bp.sample.domain.port.out.GreetingAuditPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Outbound adapter that implements {@link GreetingAuditPort} by logging events.
 * Hexagonal intent:
 * - Domain only knows about GreetingAuditPort
 * - Infrastructure decides how to implement it (here: logging)
 * This class is intentionally kept free of Spring annotations.
 * It will be registered as a bean from a bootstrap @Configuration class.
 */
public class LoggingGreetingAuditAdapter implements GreetingAuditPort {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingGreetingAuditAdapter.class);

    @Override
    public void auditCreated(Greeting greeting) {
        // Very simple implementation for the BASIC sample:
        // just log that a greeting was created.
        if (greeting == null) {
            log.warn("GreetingAuditPort.auditCreated invoked with null Greeting");
            return;
        }

        log.info(
                "Greeting created: id={}, text={}",
                greeting.id(),
                greeting.text().value()
        );
    }
}