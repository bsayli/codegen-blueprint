package ${projectPackageName}.bp.sample.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class GreetingTextTest {

    @Test
    @DisplayName("of(raw) trims and keeps value")
    void of_trims() {
        GreetingText text = GreetingText.of("  hello  ");
        assertThat(text.value()).isEqualTo("hello");
    }

    @Test
    @DisplayName("null value should fail")
    void null_shouldFail() {
        assertThatThrownBy(() -> new GreetingText(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("blank value should fail")
    void blank_shouldFail() {
        assertThatThrownBy(() -> GreetingText.of("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Greeting text must not be blank");
    }

    @Test
    @DisplayName("length > 140 should fail")
    void tooLong_shouldFail() {
        String raw = "a".repeat(141);

        assertThatThrownBy(() -> GreetingText.of(raw))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Greeting text must not exceed");
    }
}