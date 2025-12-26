package ${projectPackageName}.application.sample.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import ${projectPackageName}.application.sample.port.in.model.GetGreetingQuery;
import ${projectPackageName}.application.sample.port.in.model.GetGreetingResult;
import ${projectPackageName}.domain.sample.model.Greeting;
import ${projectPackageName}.domain.sample.port.out.GreetingAuditPort;
import ${projectPackageName}.domain.sample.service.GreetingService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("application")
class GetGreetingHandlerTest {

    @Test
    @DisplayName("getDefault() returns default greeting and audits once")
    void getDefault_returnsDefault_andAudits() {
        RecordingAuditPort audit = new RecordingAuditPort();
        GetGreetingHandler handler = new GetGreetingHandler(new GreetingService(), audit);

        GetGreetingResult result = handler.getDefault();

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.text()).isEqualTo("Hello from hexagonal sample!");

        assertThat(audit.created).hasSize(1);
        assertThat(audit.created.getFirst().text().value()).isEqualTo("Hello from hexagonal sample!");
    }

    @Test
    @DisplayName("getPersonal(null query) falls back to default and audits")
    void getPersonal_nullQuery_fallsBack() {
        RecordingAuditPort audit = new RecordingAuditPort();
        GetGreetingHandler handler = new GetGreetingHandler(new GreetingService(), audit);

        GetGreetingResult result = handler.getPersonal(null);

        assertThat(result.text()).isEqualTo("Hello from hexagonal sample!");
        assertThat(audit.created).hasSize(1);
    }

    @Test
    @DisplayName("getPersonal(query with null name) falls back to default and audits")
    void getPersonal_nullName_fallsBack() {
        RecordingAuditPort audit = new RecordingAuditPort();
        GetGreetingHandler handler = new GetGreetingHandler(new GreetingService(), audit);

        GetGreetingResult result = handler.getPersonal(GetGreetingQuery.of(null));

        assertThat(result.text()).isEqualTo("Hello from hexagonal sample!");
        assertThat(audit.created).hasSize(1);
    }

    @Test
    @DisplayName("getPersonal(name) returns personalized greeting and audits")
    void getPersonal_name_personalizes() {
        RecordingAuditPort audit = new RecordingAuditPort();
        GetGreetingHandler handler = new GetGreetingHandler(new GreetingService(), audit);

        GetGreetingResult result = handler.getPersonal(GetGreetingQuery.of("John"));

        assertThat(result.text()).isEqualTo("Hello, John!");
        assertThat(audit.created).hasSize(1);
        assertThat(audit.created.getFirst().text().value()).isEqualTo("Hello, John!");
    }

    static final class RecordingAuditPort implements GreetingAuditPort {
        final List<Greeting> created = new ArrayList<>();

        @Override
        public void auditCreated(Greeting greeting) {
            created.add(greeting);
        }
    }
}