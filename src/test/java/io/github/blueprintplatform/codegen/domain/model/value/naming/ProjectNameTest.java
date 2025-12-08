package io.github.blueprintplatform.codegen.domain.model.value.naming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class ProjectNameTest {

  @Test
  @DisplayName("valid raw value should be trimmed and accepted as-is")
  void validValue_shouldTrimAndAccept() {
    ProjectName name = new ProjectName("  My  Project_Name  ");

    assertThat(name.value()).isEqualTo("My  Project_Name");
  }

  @Test
  @DisplayName("null should throw NOT_BLANK violation with correct message key")
  void nullValue_shouldThrowNotBlank() {
    assertThatThrownBy(() -> new ProjectName(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.not.blank");
            });
  }

  @Test
  @DisplayName("too short value should fail LENGTH rule")
  void tooShort_shouldFailLengthRule() {
    assertThatThrownBy(() -> new ProjectName("ab"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.length");
            });
  }

  @Test
  @DisplayName("too long value should fail LENGTH rule")
  void tooLong_shouldFailLengthRule() {
    String longName = "A".repeat(61); // MAX = 60

    assertThatThrownBy(() -> new ProjectName(longName))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.length");
            });
  }

  @Test
  @DisplayName("value with invalid characters should fail INVALID_CHARS rule")
  void invalidCharacters_shouldFailInvalidCharsRule() {
    assertThatThrownBy(() -> new ProjectName("my$app"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.invalid.chars");
            });
  }

  @Test
  @DisplayName("value with allowed punctuation should be accepted")
  void allowedPunctuation_shouldBeAccepted() {
    ProjectName name = new ProjectName("My Project, v1.0 (LTS)_alpha");

    assertThat(name.value()).isEqualTo("My Project, v1.0 (LTS)_alpha");
  }
}
