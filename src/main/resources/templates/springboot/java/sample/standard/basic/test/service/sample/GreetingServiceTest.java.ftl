package ${projectPackageName}.service.sample;

import static org.assertj.core.api.Assertions.assertThat;

import ${projectPackageName}.domain.sample.model.Greeting;
import ${projectPackageName}.domain.sample.service.GreetingDomainService;
import ${projectPackageName}.repository.sample.GreetingAuditRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("service")
class GreetingServiceTest {

    @Test
    @DisplayName("getDefaultGreeting() returns default greeting and audits once")
    void getDefaultGreeting_returnsDefault_andAudits() {
        RecordingAuditRepository audit = new RecordingAuditRepository();
        GreetingService service =
                new GreetingService(new GreetingDomainService(), audit);

        Greeting greeting = service.getDefaultGreeting();

        assertThat(greeting).isNotNull();
        assertThat(greeting.id().value()).isNotNull();
        assertThat(greeting.text().value())
                .isEqualTo("Hello from layered sample!");

        assertThat(audit.created).hasSize(1);
        assertThat(audit.created.getFirst().text().value())
                .isEqualTo("Hello from layered sample!");
    }

    @Test
    @DisplayName("getPersonalGreeting(null) falls back to default name and audits")
    void getPersonalGreeting_nullName_fallsBack_andAudits() {
        RecordingAuditRepository audit = new RecordingAuditRepository();
        GreetingService service =
                new GreetingService(new GreetingDomainService(), audit);

        Greeting greeting = service.getPersonalGreeting(null);

        assertThat(greeting.text().value())
                .isEqualTo("Hello from layered sample!");

        assertThat(audit.created).hasSize(1);
        assertThat(audit.created.getFirst().text().value())
                .isEqualTo("Hello from layered sample!");
    }

    @Test
    @DisplayName("getPersonalGreeting(name) personalizes and audits")
    void getPersonalGreeting_name_personalizes_andAudits() {
        RecordingAuditRepository audit = new RecordingAuditRepository();
        GreetingService service =
                new GreetingService(new GreetingDomainService(), audit);

        Greeting greeting = service.getPersonalGreeting("John");

        assertThat(greeting.text().value())
                .isEqualTo("Hello, John!");

        assertThat(audit.created).hasSize(1);
        assertThat(audit.created.getFirst().text().value())
                .isEqualTo("Hello, John!");
    }

    /**
     * Test-only audit repository to record side effects.
     * Mirrors the RecordingAuditPort from the hexagonal sample.
     */
    static final class RecordingAuditRepository implements GreetingAuditRepository {

        final List<Greeting> created = new ArrayList<>();

        @Override
        public void auditCreated(Greeting greeting) {
            created.add(greeting);
        }
    }
}