package ${projectPackageName}.repository.sample;

import ${projectPackageName}.domain.sample.model.Greeting;

/**
 * Repository-like abstraction for audit side effects.
 * Intent:
 * - keep the sample focused on "side effect after a use-case"
 * - avoid persistence concerns (no save/query here)
 * - allow swapping implementations (logging, DB, messaging) later
 */
public interface GreetingAuditRepository {

    void auditCreated(Greeting greeting);
}