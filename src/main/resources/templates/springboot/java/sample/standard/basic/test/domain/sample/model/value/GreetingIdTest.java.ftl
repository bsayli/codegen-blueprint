package ${projectPackageName}.domain.sample.model.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
@Tag("domain")
class GreetingIdTest {

    @Test
    @DisplayName("newId() should create non-null UUID")
    void newId_shouldCreate() {
        GreetingId id = GreetingId.newId();
        assertThat(id.value()).isNotNull();
    }

    @Test
    @DisplayName("fromString(raw) should parse UUID")
    void fromString_shouldParse() {
        UUID uuid = UUID.randomUUID();
        GreetingId id = GreetingId.fromString(uuid.toString());

        assertThat(id.value()).isEqualTo(uuid);
        assertThat(id.toString()).hasToString(uuid.toString());
    }

    @Test
    @DisplayName("null UUID should fail")
    void null_shouldFail() {
        assertThatThrownBy(() -> new GreetingId(null))
                .isInstanceOf(NullPointerException.class);
    }
}