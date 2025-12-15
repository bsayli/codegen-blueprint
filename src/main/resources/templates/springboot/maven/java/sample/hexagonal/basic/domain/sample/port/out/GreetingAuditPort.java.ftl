package ${projectPackageName}.domain.sample.port.out;

import ${projectPackageName}.domain.sample.model.Greeting;

/**
* Outbound port for auditing greeting creations.
* Hexagonal intent:
* - Domain expresses the need for persistence/logging
* - Without depending on any technology (DB, File, Kafka, etc.)
*/
public interface GreetingAuditPort {

/**
* Publish or persist that a Greeting was created.
* Infrastructure (adapter) decides how (log, file, DB, etc.)
*/
void auditCreated(Greeting greeting);
}