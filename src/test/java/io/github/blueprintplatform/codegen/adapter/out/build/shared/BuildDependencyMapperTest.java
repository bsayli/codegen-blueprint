package io.github.blueprintplatform.codegen.adapter.out.build.shared;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyCoordinates;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyScope;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.DependencyVersion;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ArtifactId;
import io.github.blueprintplatform.codegen.domain.model.value.identity.GroupId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("adapter")
class BuildDependencyMapperTest {

  private final BuildDependencyMapper mapper = new BuildDependencyMapper();

  private static Dependency dep(
      String groupId, String artifactId, String version, DependencyScope scope) {
    DependencyVersion v = (version == null) ? null : new DependencyVersion(version);
    return new Dependency(
        new DependencyCoordinates(new GroupId(groupId), new ArtifactId(artifactId)), v, scope);
  }

  @Test
  @DisplayName("from(empty Dependencies) should return empty list")
  void from_emptyDependencies_shouldReturnEmptyList() {
    Dependencies deps = Dependencies.of(List.of());

    List<BuildDependency> result = mapper.from(deps);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("from(Dependencies) should map coordinates, optional version and scope correctly")
  void from_dependencies_shouldMapFieldsCorrectly() {
    Dependency d1 = dep("org.acme", "alpha", null, null);
    Dependency d2 = dep("org.acme", "beta", "1.2.3", DependencyScope.RUNTIME);
    Dependency d3 = dep("org.acme", "gamma", "2.0.0-RC1", DependencyScope.TEST);

    Dependencies deps = Dependencies.of(List.of(d1, d2, d3));

    List<BuildDependency> result = mapper.from(deps);

    assertThat(result).hasSize(3);

    Map<String, BuildDependency> byArtifactId =
        result.stream().collect(Collectors.toMap(BuildDependency::artifactId, p -> p));

    BuildDependency alpha = byArtifactId.get("alpha");
    assertThat(alpha.groupId()).isEqualTo("org.acme");
    assertThat(alpha.version()).isNull();
    assertThat(alpha.scope()).isNull();

    BuildDependency beta = byArtifactId.get("beta");
    assertThat(beta.groupId()).isEqualTo("org.acme");
    assertThat(beta.version()).isEqualTo("1.2.3");
    assertThat(beta.scope()).isEqualTo("runtime");

    BuildDependency gamma = byArtifactId.get("gamma");
    assertThat(gamma.groupId()).isEqualTo("org.acme");
    assertThat(gamma.version()).isEqualTo("2.0.0-RC1");
    assertThat(gamma.scope()).isEqualTo("test");
  }

  @Test
  @DisplayName("from(Dependency) should map single dependency to PomDependency")
  void from_singleDependency_shouldMapToPomDependency() {
    Dependency domainDep =
        dep("com.example", "demo-lib", "0.9.0-SNAPSHOT", DependencyScope.PROVIDED);

    BuildDependency pomDep = mapper.from(domainDep);

    assertThat(pomDep.groupId()).isEqualTo("com.example");
    assertThat(pomDep.artifactId()).isEqualTo("demo-lib");
    assertThat(pomDep.version()).isEqualTo("0.9.0-SNAPSHOT");
    assertThat(pomDep.scope()).isEqualTo("provided");
  }
}
