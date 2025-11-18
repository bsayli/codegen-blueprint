package io.github.bsayli.codegen.initializr.domain.policy.tech;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: CompatibilityPolicy")
class CompatibilityPolicyTest {

  private static BuildOptions buildOptions() {
    return new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
  }

  private static PlatformTarget target(JavaVersion java, SpringBootVersion boot) {
    return new PlatformTarget(java, boot);
  }

  @Test
  @DisplayName("ensureCompatible should fail when options or target is null")
  @SuppressWarnings("DataFlowIssue")
  void ensureCompatible_nullOptionsOrTarget_shouldFailTargetMissing() {
    BuildOptions options = buildOptions();
    PlatformTarget target = target(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(null, target))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(options, null))
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
    BuildOptions options = buildOptions();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    options, target(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6)))
        .doesNotThrowAnyException();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    options, target(JavaVersion.JAVA_25, SpringBootVersion.V3_5_6)))
        .doesNotThrowAnyException();

    assertThatCode(
            () ->
                CompatibilityPolicy.ensureCompatible(
                    options, target(JavaVersion.JAVA_21, SpringBootVersion.V3_4_10)))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("ensureCompatible should fail for incompatible Spring Boot / Java combinations")
  void ensureCompatible_incompatibleTarget_shouldFail() {
    BuildOptions options = buildOptions();
    PlatformTarget incompatible = target(JavaVersion.JAVA_25, SpringBootVersion.V3_4_10);

    assertThatThrownBy(() -> CompatibilityPolicy.ensureCompatible(options, incompatible))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> {
              assertThat(dve.getMessageKey()).isEqualTo("platform.target.incompatible");
              assertThat(dve.getArgs())
                  .containsExactly(
                      incompatible.springBoot().value(), incompatible.java().asString());
            });
  }

  @Test
  @DisplayName("allowedJavaFor should return supported Java versions for each Spring Boot version")
  void allowedJavaFor_shouldReturnSupportedJavaVersions() {
    Set<JavaVersion> for3510 = CompatibilityPolicy.allowedJavaFor(SpringBootVersion.V3_4_10);
    Set<JavaVersion> for356 = CompatibilityPolicy.allowedJavaFor(SpringBootVersion.V3_5_6);

    assertThat(for3510).containsExactlyInAnyOrder(JavaVersion.JAVA_21);
    assertThat(for356).containsExactlyInAnyOrder(JavaVersion.JAVA_21, JavaVersion.JAVA_25);
  }

  @Test
  @DisplayName("allSupportedTargets should return all combinations defined in the matrix")
  void allSupportedTargets_shouldReturnAllMatrixCombinations() {
    List<PlatformTarget> targets = CompatibilityPolicy.allSupportedTargets();

    assertThat(targets)
        .containsExactlyInAnyOrder(
            target(JavaVersion.JAVA_21, SpringBootVersion.V3_4_10),
            target(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6),
            target(JavaVersion.JAVA_25, SpringBootVersion.V3_5_6));
  }
}
