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
@DisplayName("Unit Test: DependencyCoordinates")
class DependencyCoordinatesTest {

  @Test
  @DisplayName("valid groupId and artifactId should be accepted")
  void validCoordinates_shouldBeAccepted() {
    GroupId g = new GroupId("org.acme");
    ArtifactId a = new ArtifactId("demo-artifact");

    DependencyCoordinates coords = new DependencyCoordinates(g, a);

    assertThat(coords.groupId()).isSameAs(g);
    assertThat(coords.artifactId()).isSameAs(a);
  }

  @Test
  @DisplayName("null groupId should fail COORDINATES_REQUIRED")
  void nullGroupId_shouldFailCoordinatesRequired() {
    ArtifactId a = new ArtifactId("demo-artifact");

    assertThatThrownBy(() -> new DependencyCoordinates(null, a))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.coordinates.not.blank");
            });
  }

  @Test
  @DisplayName("null artifactId should fail COORDINATES_REQUIRED")
  void nullArtifactId_shouldFailCoordinatesRequired() {
    GroupId g = new GroupId("org.acme");

    assertThatThrownBy(() -> new DependencyCoordinates(g, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.coordinates.not.blank");
            });
  }
}
