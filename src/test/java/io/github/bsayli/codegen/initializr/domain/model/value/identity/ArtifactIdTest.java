package io.github.bsayli.codegen.initializr.domain.model.value.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: ArtifactId")
class ArtifactIdTest {

  @Test
  @DisplayName("valid raw value should be normalized and accepted")
  void validValue_shouldNormalizeAndAccept() {
    ArtifactId id = new ArtifactId("  My_Artifact   Id  ");

    assertThat(id.value()).isEqualTo("my-artifact-id");
  }

  @Test
  @DisplayName("null should throw NOT_BLANK violation with correct message key")
  void nullValue_shouldThrowNotBlank() {
    assertThatThrownBy(() -> new ArtifactId(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.not.blank");
            });
  }

  @Test
  @DisplayName("too short value should fail LENGTH rule")
  void tooShort_shouldFailLengthRule() {
    assertThatThrownBy(() -> new ArtifactId("ab"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.length");
            });
  }

  @Test
  @DisplayName("value with invalid characters should fail INVALID_CHARS rule")
  void invalidCharacters_shouldFailInvalidCharsRule() {
    assertThatThrownBy(() -> new ArtifactId("my$app"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.invalid.chars");
            });
  }

  @Test
  @DisplayName("value starting with non letter should fail STARTS_WITH_LETTER rule")
  void startsWithNonLetter_shouldFailStartsWithLetterRule() {
    assertThatThrownBy(() -> new ArtifactId("1artifact"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.starts.with.letter");
            });
  }

  @Test
  @DisplayName("leading dash should fail STARTS_WITH_LETTER rule")
  void leadingDash_shouldFailStartsWithLetterRule() {
    assertThatThrownBy(() -> new ArtifactId("-artifact"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.starts.with.letter");
            });
  }

  @Test
  @DisplayName("trailing dash should fail EDGE_CHAR rule")
  void trailingDash_shouldFailEdgeCharRule() {
    assertThatThrownBy(() -> new ArtifactId("artifact-"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.artifact-id.edge.char");
            });
  }

  @Test
  @DisplayName("multiple consecutive dashes are normalized to a single dash")
  void consecutiveDashes_areNormalizedToSingleDash() {
    ArtifactId id = new ArtifactId("my--artifact---id");

    assertThat(id.value()).isEqualTo("my-artifact-id");
  }
}
