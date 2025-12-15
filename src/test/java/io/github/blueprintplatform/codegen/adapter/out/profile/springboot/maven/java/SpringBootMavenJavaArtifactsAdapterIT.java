package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.domain.factory.ProjectBlueprintFactory;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.EnforcementMode;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
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
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
class SpringBootMavenJavaArtifactsAdapterIT {

  @Autowired private SpringBootMavenJavaArtifactsAdapter adapter;

  @Test
  @DisplayName(
      "generate() should produce artifacts for a valid Spring Boot + Maven + Java blueprint")
  void generate_shouldProduceArtifactsForValidBlueprint() {
    ProjectBlueprint blueprint = blueprint();

    Iterable<? extends GeneratedResource> files = adapter.generate(blueprint);

    var list = StreamSupport.stream(files.spliterator(), false).toList();

    assertThat(list).isNotEmpty();
  }

  private ProjectBlueprint blueprint() {
    ProjectMetadata metadata =
        new ProjectMetadata(
            new ProjectIdentity(new GroupId("com.example"), new ArtifactId("demo-app")),
            new ProjectName("demo-app"),
            new ProjectDescription("Integration test blueprint"),
            new PackageName("com.example.demo"));

    TechStack techStack = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
    PlatformTarget platformTarget =
        new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5);
    PlatformSpec platform = new PlatformSpec(techStack, platformTarget);

    ArchitectureSpec architecture =
        new ArchitectureSpec(
            ProjectLayout.STANDARD,
            new ArchitectureGovernance(EnforcementMode.NONE),
            SampleCodeOptions.none());

    Dependency webStarter =
        new Dependency(
            new DependencyCoordinates(
                new GroupId("org.springframework.boot"), new ArtifactId("spring-boot-starter")),
            null,
            null);

    Dependencies dependencies = Dependencies.of(List.of(webStarter));

    return ProjectBlueprintFactory.of(metadata, platform, architecture, dependencies);
  }
}
