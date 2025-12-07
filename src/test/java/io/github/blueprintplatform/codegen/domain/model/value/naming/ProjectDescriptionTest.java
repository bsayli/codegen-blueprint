package io.github.blueprintplatform.codegen.domain.model.value.naming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class ProjectDescriptionTest {

  @Test
  @DisplayName("valid description should be normalized and accepted")
  void validDescription_shouldNormalizeAndAccept() {
    ProjectDescription desc = new ProjectDescription("  This  is  a  test  ");

    assertThat(desc.value()).isEqualTo("This is a test");
    assertThat(desc.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("null description should fail NOT_BLANK rule")
  void nullDescription_shouldFailNotBlankRule() {
    assertThatThrownBy(() -> new ProjectDescription(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.description.not.blank");
            });
  }

  @Test
  @DisplayName("blank description should fail NOT_BLANK rule")
  void blankDescription_shouldFailNotBlankRule() {
    assertThatThrownBy(() -> new ProjectDescription("    "))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.description.not.blank");
            });
  }

  @Test
  @DisplayName("too short description should fail LENGTH rule")
  void tooShortDescription_shouldFailLengthRule() {
    // 9 karakter, MIN = 10 altında
    String shortText = "too short";

    assertThatThrownBy(() -> new ProjectDescription(shortText))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.description.length");
            });
  }

  @Test
  @DisplayName("too long description should fail LENGTH rule")
  void tooLongDescription_shouldFailLengthRule() {
    String longText = "a".repeat(281);

    assertThatThrownBy(() -> new ProjectDescription(longText))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.description.length");
            });
  }

  @Test
  @DisplayName("description with control chars should fail CONTROL_CHARS rule")
  void controlChars_shouldFailInvalidCharsRule() {
    String bad = "valid" + '\u0001' + "text";

    assertThatThrownBy(() -> new ProjectDescription(bad))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.description.control.chars");
            });
  }
}
