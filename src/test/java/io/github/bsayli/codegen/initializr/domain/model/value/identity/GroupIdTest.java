package io.github.bsayli.codegen.initializr.domain.model.value.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: GroupId")
class GroupIdTest {

  @Test
  @DisplayName("valid raw value should be normalized and accepted")
  void validValue_shouldNormalizeAndAccept() {
    GroupId groupId = new GroupId("  Com.Example.App  ");

    assertThat(groupId.value()).isEqualTo("com.example.app");
  }

  @Test
  @DisplayName("null should throw NOT_BLANK violation with correct message key")
  void nullValue_shouldThrowNotBlank() {
    assertThatThrownBy(() -> new GroupId(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.group-id.not.blank");
            });
  }

  @Test
  @DisplayName("too short value should fail LENGTH rule")
  void tooShort_shouldFailLengthRule() {
    assertThatThrownBy(() -> new GroupId("ab"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.group-id.length");
            });
  }

  @Test
  @DisplayName("value with invalid segment format should fail SEGMENT_FORMAT rule")
  void invalidSegmentFormat_shouldFailSegmentFormatRule() {
    assertThatThrownBy(() -> new GroupId("com..example"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.group-id.segment.format");
            });
  }

  @Test
  @DisplayName("segment starting with non-letter should fail SEGMENT_FORMAT rule")
  void segmentStartingWithNonLetter_shouldFailSegmentFormatRule() {
    assertThatThrownBy(() -> new GroupId("com.1example"))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.group-id.segment.format");
            });
  }
}
