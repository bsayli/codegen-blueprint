package io.github.bsayli.codegen.initializr.domain.policy.tech;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: PlatformTargetSelector")
class PlatformTargetSelectorTest {

  private static BuildOptions buildOptions() {
    return new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
  }

  @Test
  @DisplayName("select with compatible target should return PlatformTarget")
  void select_compatibleTarget_shouldReturnRequestedTarget() {
    BuildOptions options = buildOptions();

    PlatformTarget result =
        PlatformTargetSelector.select(options, JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);

    assertThat(result.java()).isEqualTo(JavaVersion.JAVA_21);
    assertThat(result.springBoot()).isEqualTo(SpringBootVersion.V3_5_6);
  }

  @Test
  @DisplayName("select with incompatible target should delegate to CompatibilityPolicy and fail")
  void select_incompatibleTarget_shouldFailCompatibility() {
    BuildOptions options = buildOptions();

    assertThatThrownBy(
            () ->
                PlatformTargetSelector.select(
                    options, JavaVersion.JAVA_25, SpringBootVersion.V3_4_10))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.incompatible"));
  }

  @Test
  @DisplayName("supportedTargetsFor should return all supported targets from CompatibilityPolicy")
  void supportedTargetsFor_shouldReturnAllSupportedTargets() {
    BuildOptions options = buildOptions();

    List<PlatformTarget> targets = PlatformTargetSelector.supportedTargetsFor(options);

    assertThat(targets)
        .isNotEmpty()
        .contains(new PlatformTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6));
  }
}
