package io.github.bsayli.codegen.initializr.domain.model.value.tech.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: BuildOptions")
class BuildOptionsTest {

  @Test
  @DisplayName("valid framework, buildTool and language should be accepted")
  void validOptions_shouldBeAccepted() {
    BuildOptions options = new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    assertThat(options.framework()).isEqualTo(Framework.SPRING_BOOT);
    assertThat(options.buildTool()).isEqualTo(BuildTool.MAVEN);
    assertThat(options.language()).isEqualTo(Language.JAVA);
  }

  @Test
  @DisplayName("null framework should fail BUILD_OPTIONS_REQUIRED")
  void nullFramework_shouldFailBuildOptionsRequired() {
    assertThatThrownBy(() -> new BuildOptions(null, BuildTool.MAVEN, Language.JAVA))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.build-options.not.blank");
            });
  }

  @Test
  @DisplayName("null buildTool should fail BUILD_OPTIONS_REQUIRED")
  void nullBuildTool_shouldFailBuildOptionsRequired() {
    assertThatThrownBy(() -> new BuildOptions(Framework.SPRING_BOOT, null, Language.JAVA))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.build-options.not.blank");
            });
  }

  @Test
  @DisplayName("null language should fail BUILD_OPTIONS_REQUIRED")
  void nullLanguage_shouldFailBuildOptionsRequired() {
    assertThatThrownBy(() -> new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.build-options.not.blank");
            });
  }
}
