package io.github.bsayli.codegen.initializr.domain.model.value.pkg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: PackageName")
class PackageNameTest {

  @Test
  @DisplayName("valid raw value should be normalized and accepted")
  void validValue_shouldNormalizeAndAccept() {
    PackageName pkg = new PackageName("  Com_Example-Api  ");

    assertThat(pkg.value()).isEqualTo("com.example.api");
  }

  @Test
  @DisplayName("null should throw NOT_BLANK violation with correct message key")
  void nullValue_shouldThrowNotBlank() {
    assertThatThrownBy(() -> new PackageName(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.not.blank");
            });
  }

  @Test
  @DisplayName("blank or only separators should throw NOT_BLANK after normalization")
  void blankOrOnlySeparators_shouldThrowNotBlankAfterNormalization() {
    assertThatThrownBy(() -> new PackageName("   "))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.not.blank");
            });

    assertThatThrownBy(() -> new PackageName(" - _  - "))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.not.blank");
            });
  }

  @Test
  @DisplayName("too short normalized value should fail LENGTH rule")
  void tooShort_shouldFailLengthRule() {
    assertThatThrownBy(() -> new PackageName("ab"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.length");
            });
  }

  @Test
  @DisplayName("segment with invalid format should fail SEGMENT_FORMAT rule")
  void invalidSegmentFormat_shouldFailSegmentFormatRule() {
    assertThatThrownBy(() -> new PackageName("com.1example"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.segment.format");
            });
  }

  @Test
  @DisplayName("reserved prefixes (java, javax, sun, com.sun) should fail RESERVED_PREFIX rule")
  void reservedPrefix_shouldFailReservedPrefixRule() {
    assertThatThrownBy(() -> new PackageName("java.util"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.reserved.prefix");
            });

    assertThatThrownBy(() -> new PackageName("javax.mail"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.reserved.prefix");
            });

    assertThatThrownBy(() -> new PackageName("com.sun.tools"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.package-name.reserved.prefix");
            });
  }
}
