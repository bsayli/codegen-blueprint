package ${projectPackageName}.bp.sample.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import ${projectPackageName}.bp.sample.domain.model.Greeting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class GreetingDomainServiceTest {

    private final GreetingDomainService service = new GreetingDomainService();

    @Test
    @DisplayName("defaultGreeting() returns default text")
    void defaultGreeting_returnsDefault() {
        Greeting greeting = service.defaultGreeting();

        assertThat(greeting).isNotNull();
        assertThat(greeting.id().value()).isNotNull();
        assertThat(greeting.text().value()).isEqualTo("Hello from layered sample!");
    }

    @Test
    @DisplayName("createGreeting(raw) enforces GreetingText invariants and returns Greeting")
    void createGreeting_returnsGreeting() {
        Greeting greeting = service.createGreeting(" Hello ");

        assertThat(greeting.id().value()).isNotNull();
        assertThat(greeting.text().value()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("createPersonalGreeting(null) uses fallback name")
    void createPersonalGreeting_nullName_fallback() {
        Greeting greeting = service.createPersonalGreeting(null);

        assertThat(greeting.text().value()).isEqualTo("Hello, there!");
    }

    @Test
    @DisplayName("createPersonalGreeting(blank) uses fallback name")
    void createPersonalGreeting_blankName_fallback() {
        Greeting greeting = service.createPersonalGreeting("   ");

        assertThat(greeting.text().value()).isEqualTo("Hello, there!");
    }

    @Test
    @DisplayName("createPersonalGreeting(name) personalizes")
    void createPersonalGreeting_personalizes() {
        Greeting greeting = service.createPersonalGreeting("John");

        assertThat(greeting.text().value()).isEqualTo("Hello, John!");
    }
}