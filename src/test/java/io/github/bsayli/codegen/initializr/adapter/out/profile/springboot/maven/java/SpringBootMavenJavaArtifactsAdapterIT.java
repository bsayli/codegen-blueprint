package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bsayli.codegen.initializr.domain.factory.ProjectBlueprintFactory;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.DependencyCoordinates;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
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

    Iterable<? extends GeneratedFile> files = adapter.generate(blueprint);

    var list = StreamSupport.stream(files.spliterator(), false).toList();

    assertThat(list).isNotEmpty();
  }

  private ProjectBlueprint blueprint() {
    ProjectIdentity identity =
        new ProjectIdentity(new GroupId("com.example"), new ArtifactId("demo-app"));

    ProjectName name = new ProjectName("demo-app");
    ProjectDescription description = new ProjectDescription("Integration test blueprint");
    PackageName packageName = new PackageName("com.example.demo");

    TechStack techStack =
        new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    PlatformTarget platformTarget =
        new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);

    Dependency webStarter =
        new Dependency(
            new DependencyCoordinates(
                new GroupId("org.springframework.boot"), new ArtifactId("spring-boot-starter")),
            null,
            null);

    Dependencies dependencies = Dependencies.of(List.of(webStarter));

    return ProjectBlueprintFactory.of(
        identity, name, description, packageName, techStack, platformTarget, dependencies);
  }
}
