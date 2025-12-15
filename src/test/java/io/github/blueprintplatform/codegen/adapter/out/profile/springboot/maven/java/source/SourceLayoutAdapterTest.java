package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.metadata.ProjectMetadata;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.model.value.tech.PlatformSpec;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedDirectory;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class SourceLayoutAdapterTest {

  private static final String BASE_PACKAGE = "com.acme.demo";
  private static final String BASE_PACKAGE_PATH = "com/acme/demo";

  private static ProjectBlueprint blueprint(ProjectLayout layout) {
    ProjectMetadata metadata =
            new ProjectMetadata(
                    new ProjectIdentity(new GroupId("com.acme"), new ArtifactId("demo-app")),
                    new ProjectName("Demo App"),
                    new ProjectDescription("Sample Project"),
                    new PackageName(BASE_PACKAGE));

    PlatformSpec platform =
            new PlatformSpec(
                    new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
                    new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5));

    ArchitectureSpec architecture =
            new ArchitectureSpec(
                    layout,
                    ArchitectureGovernance.none(),
                    SampleCodeOptions.none());

    Dependencies dependencies = Dependencies.of(List.of());

    return new ProjectBlueprint(metadata, platform, architecture, dependencies);
  }

  private static List<Path> toRelativePaths(Iterable<? extends GeneratedResource> resources) {
    List<Path> result = new ArrayList<>();
    StreamSupport.stream(resources.spliterator(), false)
        .forEach(
            r -> {
              assertThat(r).isInstanceOf(GeneratedDirectory.class);
              result.add(r.relativePath());
            });
    return result;
  }

  @Test
  @DisplayName("artifactKey() should return SOURCE_LAYOUT")
  void artifactKey_shouldReturnSourceLayout() {
    SourceLayoutAdapter adapter = new SourceLayoutAdapter();

    assertThat(adapter.artifactKey()).isEqualTo(ArtifactKey.SOURCE_LAYOUT);
  }

  @Test
  @DisplayName("generate() with STANDARD layout should create base and package directories only")
  void generate_standardLayout_shouldCreateBaseAndPackageDirsOnly() {
    SourceLayoutAdapter adapter = new SourceLayoutAdapter();
    ProjectBlueprint blueprint = blueprint(ProjectLayout.STANDARD);

    Iterable<? extends GeneratedResource> resources = adapter.generate(blueprint);
    List<Path> paths = toRelativePaths(resources);

    List<Path> expected =
        List.of(
            Path.of("src/main/java"),
            Path.of("src/test/java"),
            Path.of("src/main/resources"),
            Path.of("src/test/resources"),
            Path.of("src/main/java").resolve(BASE_PACKAGE_PATH),
            Path.of("src/test/java").resolve(BASE_PACKAGE_PATH));

    assertThat(paths).containsExactlyInAnyOrderElementsOf(expected).hasSize(expected.size());

    // Hexagonal’a özel alt dizinler olmamalı
    assertThat(paths)
        .noneMatch(
            p ->
                p.toString().endsWith("/adapter")
                    || p.toString().endsWith("/application")
                    || p.toString().endsWith("/bootstrap")
                    || p.toString().endsWith("/domain"));
  }

  @Test
  @DisplayName(
      "generate() with HEXAGONAL layout should create base/package directories and hexagonal sub-packages")
  void generate_hexagonalLayout_shouldCreateHexagonalSubPackages() {
    SourceLayoutAdapter adapter = new SourceLayoutAdapter();
    ProjectBlueprint blueprint = blueprint(ProjectLayout.HEXAGONAL);

    Iterable<? extends GeneratedResource> resources = adapter.generate(blueprint);
    List<Path> paths = toRelativePaths(resources);

    Path mainBase = Path.of("src/main/java").resolve(BASE_PACKAGE_PATH);
    Path testBase = Path.of("src/test/java").resolve(BASE_PACKAGE_PATH);

    List<Path> expected =
        List.of(
            // base source/resources dizinleri
            Path.of("src/main/java"),
            Path.of("src/test/java"),
            Path.of("src/main/resources"),
            Path.of("src/test/resources"),
            // base package dizinleri
            mainBase,
            testBase,
            // hexagonal alt paketleri
            mainBase.resolve("adapter"),
            mainBase.resolve("application"),
            mainBase.resolve("bootstrap"),
            mainBase.resolve("domain"));

    assertThat(paths).containsExactlyInAnyOrderElementsOf(expected).hasSize(expected.size());
  }
}
