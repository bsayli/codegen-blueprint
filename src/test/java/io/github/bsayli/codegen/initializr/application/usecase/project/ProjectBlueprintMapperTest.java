package io.github.bsayli.codegen.initializr.application.usecase.project;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyScope;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("application")
class ProjectBlueprintMapperTest {

  private static ProjectBlueprint getProjectBlueprint() {
    var mapper = new ProjectBlueprintMapper();

    var inputs =
        List.of(
            new DependencyInput("org.acme", "alpha", "", ""),
            new DependencyInput("org.acme", "beta", "1.2.3", "runtime"),
            new DependencyInput("org.acme", "gamma", "  ", "  "),
            new DependencyInput("org.acme", "delta", "2.0.0-RC1", "TeSt"));

    var cmd =
        new CreateProjectCommand(
            "com.acme",
            "demo-app",
            "Demo App",
            "desc",
            "com.acme.demo",
            new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
            JavaVersion.JAVA_21,
            SpringBootVersion.V3_5_6,
            inputs,
            Path.of("."));

    return mapper.from(cmd);
  }

  @Test
  @DisplayName("maps dependencies; handles blank version/scope and case-insensitive scope")
  void maps_dependencies_and_handles_blank_version_and_scope() {
    ProjectBlueprint bp = getProjectBlueprint();

    assertThat(bp.getDependencies().asList()).hasSize(4);

    Map<String, Dependency> byArtifact =
        bp.getDependencies().asList().stream()
            .collect(Collectors.toMap(d -> d.coordinates().artifactId().value(), d -> d));

    var alpha = byArtifact.get("alpha");
    assertThat(alpha.coordinates().groupId().value()).isEqualTo("org.acme");
    assertThat(alpha.version()).isNull();
    assertThat(alpha.scope()).isNull();
    assertThat(alpha.isDefaultScope()).isTrue();

    var beta = byArtifact.get("beta");
    assertThat(beta.version().value()).isEqualTo("1.2.3");
    assertThat(beta.scope()).isEqualTo(DependencyScope.RUNTIME);

    var gamma = byArtifact.get("gamma");
    assertThat(gamma.version()).isNull();
    assertThat(gamma.scope()).isNull();
    assertThat(gamma.isDefaultScope()).isTrue();

    var delta = byArtifact.get("delta");
    assertThat(delta.version().value()).isEqualTo("2.0.0-RC1");
    assertThat(delta.scope()).isEqualTo(DependencyScope.TEST);
  }
}
