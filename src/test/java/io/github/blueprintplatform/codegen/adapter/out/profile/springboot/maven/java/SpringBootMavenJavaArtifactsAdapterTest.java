package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureGovernance;
import io.github.blueprintplatform.codegen.domain.model.value.architecture.ArchitectureSpec;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class SpringBootMavenJavaArtifactsAdapterTest {

  private static ProjectBlueprint blueprint() {
    return new ProjectBlueprint(
        null,
        null,
        new ArchitectureSpec(null, ArchitectureGovernance.none(), SampleCodeOptions.none()),
        Dependencies.of(List.of()));
  }

  @Test
  @DisplayName("Should return empty list when no artifact ports are configured")
  void shouldReturnEmptyWhenNoPorts() {
    SpringBootMavenJavaArtifactsAdapter adapter =
        new SpringBootMavenJavaArtifactsAdapter(List.of());

    ProjectBlueprint blueprint = blueprint();

    List<? extends GeneratedResource> result =
        StreamSupport.stream(adapter.generate(blueprint).spliterator(), false).toList();

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should delegate generate() exactly once to each ArtifactPort")
  void shouldDelegateToEachArtifactPort() {
    ProjectBlueprint blueprint = blueprint();

    ArtifactPort p1 = mock(ArtifactPort.class);
    ArtifactPort p2 = mock(ArtifactPort.class);

    when(p1.generate(blueprint)).thenReturn(Collections.emptyList());
    when(p2.generate(blueprint)).thenReturn(Collections.emptyList());

    SpringBootMavenJavaArtifactsAdapter adapter =
        new SpringBootMavenJavaArtifactsAdapter(List.of(p1, p2));

    adapter.generate(blueprint);

    verify(p1, times(1)).generate(blueprint);
    verify(p2, times(1)).generate(blueprint);
  }
}
