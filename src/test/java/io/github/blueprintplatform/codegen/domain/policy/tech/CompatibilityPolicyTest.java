package io.github.blueprintplatform.codegen.domain.policy.tech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class CompatibilityPolicyTest {

  private static TechStack techStack() {
    return new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
  }

  private static PlatformTarget target(JavaVersion java, SpringBootVersion boot) {
    return new SpringBootJvmTarget(java, boot);
  }

  @Test
  @DisplayName("ensureCompatible should fail when techStack or target is null")
  @SuppressWarnings("DataFlowIssue")
  void ensureCompatible_nullTechStackOrTarget_shouldFailTargetMissing() {
    TechStack stack = techStack();
    PlatformTarget target = target(JavaVersion.JAVA_21, SpringBootVersion.V3_5);

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(null, target))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(stack, null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(null, null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));
  }

  @Test
  @DisplayName("ensureCompatible should accept all supported Spring Boot / Java combinations")
  void ensureCompatible_supportedTargets_shouldPass() {
    TechStack stack = techStack();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    stack, target(JavaVersion.JAVA_21, SpringBootVersion.V3_5)))
        .doesNotThrowAnyException();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    stack, target(JavaVersion.JAVA_25, SpringBootVersion.V3_5)))
        .doesNotThrowAnyException();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    stack, target(JavaVersion.JAVA_21, SpringBootVersion.V3_4)))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("ensureCompatible should fail for incompatible Spring Boot / Java combinations")
  void ensureCompatible_incompatibleTarget_shouldFail() {
    TechStack stack = techStack();
    PlatformTarget incompatible = target(JavaVersion.JAVA_25, SpringBootVersion.V3_4);

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(stack, incompatible))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> {
              assertThat(dve.getMessageKey()).isEqualTo("platform.target.incompatible");
              assertThat(dve.getArgs())
                  .containsExactly(
                      SpringBootVersion.V3_4.defaultVersion(), JavaVersion.JAVA_25.asString());
            });
  }

  @Test
  @DisplayName("allSupportedTargets should return all combinations defined in the matrix")
  void allSupportedTargets_shouldReturnAllMatrixCombinations() {
    List<PlatformTarget> targets = CompatibilityPolicy.allSupportedTargets();

    assertThat(targets)
        .containsExactlyInAnyOrder(
            target(JavaVersion.JAVA_21, SpringBootVersion.V3_4),
            target(JavaVersion.JAVA_21, SpringBootVersion.V3_5),
            target(JavaVersion.JAVA_25, SpringBootVersion.V3_5));
  }
}
