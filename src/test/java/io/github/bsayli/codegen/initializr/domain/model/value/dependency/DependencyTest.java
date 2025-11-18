package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: Dependency")
class DependencyTest {

  private static DependencyCoordinates coords(String groupId, String artifactId) {
    return new DependencyCoordinates(new GroupId(groupId), new ArtifactId(artifactId));
  }

  @Test
  @DisplayName("non-null coordinates should be accepted")
  void nonNullCoordinates_shouldBeAccepted() {
    DependencyCoordinates coords = coords("org.acme", "demo");
    Dependency d = new Dependency(coords, new DependencyVersion("1.0.0"), DependencyScope.RUNTIME);

    assertThat(d.coordinates()).isSameAs(coords);
    assertThat(d.version().value()).isEqualTo("1.0.0");
    assertThat(d.scope()).isEqualTo(DependencyScope.RUNTIME);
  }

  @Test
  @DisplayName("null coordinates should fail COORDINATES_REQUIRED")
  void nullCoordinates_shouldFailCoordinatesRequired() {
    assertThatThrownBy(() -> new Dependency(null, null, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.coordinates.not.blank");
            });
  }

  @Test
  @DisplayName("isDefaultScope should be true when scope is null")
  void isDefaultScope_shouldBeTrueWhenScopeIsNull() {
    Dependency d = new Dependency(coords("org.acme", "demo"), new DependencyVersion("1.0.0"), null);

    assertThat(d.isDefaultScope()).isTrue();
  }

  @Test
  @DisplayName("isDefaultScope should be true when scope is COMPILE")
  void isDefaultScope_shouldBeTrueWhenScopeIsCompile() {
    Dependency d =
        new Dependency(
            coords("org.acme", "demo"), new DependencyVersion("1.0.0"), DependencyScope.COMPILE);

    assertThat(d.isDefaultScope()).isTrue();
  }

  @Test
  @DisplayName("isDefaultScope should be false when scope is not COMPILE")
  void isDefaultScope_shouldBeFalseWhenScopeIsNotCompile() {
    Dependency d =
        new Dependency(
            coords("org.acme", "demo"), new DependencyVersion("1.0.0"), DependencyScope.RUNTIME);

    assertThat(d.isDefaultScope()).isFalse();
  }
}
