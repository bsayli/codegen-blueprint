package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class PlatformTargetTest {

  @Test
  @DisplayName("valid java and springBoot should be accepted")
  void validTarget_shouldBeAccepted() {
    SpringBootJvmTarget target =
        new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_4);

    assertThat(target.java()).isEqualTo(JavaVersion.JAVA_21);
    assertThat(target.springBoot()).isEqualTo(SpringBootVersion.V3_4);
    assertThat(target.java().asString()).isEqualTo("21");
    assertThat(target.springBoot().defaultVersion()).isEqualTo("3.4.12");
  }

  @Test
  @DisplayName("null java should fail TARGET_REQUIRED")
  void nullJava_shouldFailTargetRequired() {
    assertThatThrownBy(() -> new SpringBootJvmTarget(null, SpringBootVersion.V3_4))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("platform.target.not.blank");
            });
  }

  @Test
  @DisplayName("null springBoot should fail TARGET_REQUIRED")
  void nullSpringBoot_shouldFailTargetRequired() {
    assertThatThrownBy(() -> new SpringBootJvmTarget(JavaVersion.JAVA_21, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("platform.target.not.blank");
            });
  }

  @Test
  @DisplayName("null java and springBoot should fail TARGET_REQUIRED")
  void nullJavaAndSpringBoot_shouldFailTargetRequired() {
    assertThatThrownBy(() -> new SpringBootJvmTarget(null, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("platform.target.not.blank");
            });
  }
}
