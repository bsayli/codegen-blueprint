package io.github.bsayli.codegen.initializr.domain.model.value.naming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: ProjectName")
class ProjectNameTest {

  @Test
  @DisplayName("valid raw value should be normalized and accepted")
  void validValue_shouldNormalizeAndAccept() {
    ProjectName name = new ProjectName("  My  Project_Name  ");

    assertThat(name.value()).isEqualTo("my-project-name");
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
  @DisplayName("value starting with non letter should fail STARTS_WITH_LETTER rule")
  void startsWithNonLetter_shouldFailStartsWithLetterRule() {
    assertThatThrownBy(() -> new ProjectName("1project"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.starts.with.letter");
            });
  }

  @Test
  @DisplayName("leading dash should fail STARTS_WITH_LETTER rule")
  void leadingDash_shouldFailStartsWithLetterRule() {
    assertThatThrownBy(() -> new ProjectName("-my-project"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.starts.with.letter");
            });
  }

  @Test
  @DisplayName("trailing dash should fail EDGE_CHAR rule")
  void trailingDash_shouldFailEdgeCharRule() {
    assertThatThrownBy(() -> new ProjectName("my-project-"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.edge.char");
            });
  }

  @Test
  @DisplayName("multiple consecutive dashes are normalized to a single dash")
  void consecutiveDashes_areNormalizedToSingleDash() {
    ProjectName name = new ProjectName("my--project---demo");

    assertThat(name.value()).isEqualTo("my-project-demo");
  }

  @Test
  @DisplayName("reserved base names (CON, PRN, COM1, LPT2...) should fail RESERVED rule")
  void reservedNames_shouldFailReservedRule() {
    assertThatThrownBy(() -> new ProjectName("con"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.reserved");
            });

    assertThatThrownBy(() -> new ProjectName("COM1"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.name.reserved");
            });
  }
}
