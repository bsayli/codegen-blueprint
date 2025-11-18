package io.github.bsayli.codegen.initializr.domain.model.value.naming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: ProjectDescription")
class ProjectDescriptionTest {

  @Test
  @DisplayName("valid description should be normalized and accepted")
  void validDescription_shouldNormalizeAndAccept() {
    ProjectDescription desc = new ProjectDescription("  This  is   A  Test  ");

    assertThat(desc.value()).isEqualTo("this is a test");
    assertThat(desc.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("null description should become empty string and be valid")
  void nullDescription_shouldBecomeEmptyAndValid() {
    ProjectDescription desc = new ProjectDescription(null);

    assertThat(desc.value()).isEmpty();
    assertThat(desc.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("blank description should normalize to empty string and be valid")
  void blankDescription_shouldNormalizeToEmptyAndBeValid() {
    ProjectDescription desc = new ProjectDescription("    ");

    assertThat(desc.value()).isEmpty();
    assertThat(desc.isEmpty()).isTrue();
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
