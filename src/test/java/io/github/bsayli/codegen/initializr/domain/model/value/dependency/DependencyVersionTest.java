package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: DependencyVersion")
class DependencyVersionTest {

  @Test
  @DisplayName("valid version should be accepted as-is")
  void validVersion_shouldBeAccepted() {
    DependencyVersion v = new DependencyVersion("1.2.3");
    assertThat(v.value()).isEqualTo("1.2.3");
  }

  @Test
  @DisplayName("version with surrounding spaces should be trimmed")
  void versionWithSpaces_shouldBeTrimmed() {
    DependencyVersion v = new DependencyVersion("  1.2.3-SNAPSHOT  ");
    assertThat(v.value()).isEqualTo("1.2.3-SNAPSHOT");
  }

  @Test
  @DisplayName("null version should fail NOT_BLANK rule")
  void nullVersion_shouldFailNotBlankRule() {
    assertThatThrownBy(() -> new DependencyVersion(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.version.not.blank");
            });
  }

  @Test
  @DisplayName("blank version should fail NOT_BLANK rule")
  void blankVersion_shouldFailNotBlankRule() {
    assertThatThrownBy(() -> new DependencyVersion("   "))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.version.not.blank");
            });
  }

  @Test
  @DisplayName("too long version should fail LENGTH rule")
  void tooLongVersion_shouldFailLengthRule() {
    String longValue = "a".repeat(101);

    assertThatThrownBy(() -> new DependencyVersion(longValue))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.version.length");
            });
  }

  @Test
  @DisplayName("version with invalid chars should fail INVALID_CHARS rule")
  void invalidChars_shouldFailInvalidCharsRule() {
    String bad = "1.0.0!final";

    assertThatThrownBy(() -> new DependencyVersion(bad))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.version.invalid.chars");
            });
  }

  @Test
  @DisplayName("version with allowed special chars should be accepted")
  void allowedSpecialChars_shouldBeAccepted() {
    DependencyVersion v = new DependencyVersion("1.0.0-RC1+[classifier]_extra(1),{meta}:$var");
    assertThat(v.value()).isEqualTo("1.0.0-RC1+[classifier]_extra(1),{meta}:$var");
  }
}
