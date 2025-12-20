package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactPipelineExecutor;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
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
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class SpringBootMavenJavaArtifactsAdapterTest {

  private static ProjectBlueprint blueprint() {
    return ProjectBlueprint.of(
        new ProjectMetadata(
            new ProjectIdentity(
                new GroupId("io.github.blueprintplatform"), new ArtifactId("greeting")),
            new ProjectName("Greeting"),
            new ProjectDescription("Greeting sample built with hexagonal architecture"),
            new PackageName("io.github.blueprintplatform.greeting")),
        new PlatformSpec(
            new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA),
            new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5)),
        new ArchitectureSpec(
            ProjectLayout.STANDARD, ArchitectureGovernance.none(), SampleCodeOptions.none()),
        Dependencies.of(List.of()));
  }

  @Test
  @DisplayName("Should return whatever executor returns")
  void shouldReturnWhateverExecutorReturns() {
    ProjectBlueprint blueprint = blueprint();

    ArtifactPipelineExecutor executor = mock(ArtifactPipelineExecutor.class);
    List<ArtifactPort> ports = List.of(mock(ArtifactPort.class));

    List<GeneratedResource> expected = List.of();
    when(executor.execute(ports, blueprint)).thenReturn(expected);

    SpringBootMavenJavaArtifactsAdapter adapter =
        new SpringBootMavenJavaArtifactsAdapter(executor, ports);

    var result = adapter.generate(blueprint);

    assertThat(result).isSameAs(expected);
  }

  @Test
  @DisplayName("Should delegate to ArtifactPipelineExecutor with given ports and blueprint")
  void shouldDelegateToExecutor() {
    ProjectBlueprint blueprint = blueprint();

    ArtifactPipelineExecutor executor = mock(ArtifactPipelineExecutor.class);
    List<ArtifactPort> ports = List.of(mock(ArtifactPort.class), mock(ArtifactPort.class));

    when(executor.execute(ports, blueprint)).thenReturn(List.of());

    SpringBootMavenJavaArtifactsAdapter adapter =
        new SpringBootMavenJavaArtifactsAdapter(executor, ports);

    adapter.generate(blueprint);

    verify(executor, times(1)).execute(ports, blueprint);
    verifyNoMoreInteractions(executor);
  }
}
