package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: Dependencies")
class DependenciesTest {

  private static Dependency dep(String groupId, String artifactId) {
    return new Dependency(
        new DependencyCoordinates(new GroupId(groupId), new ArtifactId(artifactId)), null, null);
  }

  @Test
  @DisplayName("of(null) should fail with LIST_REQUIRED")
  void of_nullList_shouldFailListRequired() {
    assertThatThrownBy(() -> Dependencies.of(null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.list.not.blank");
            });
  }

  @Test
  @DisplayName("of(emptyList) should return empty Dependencies")
  void of_emptyList_shouldReturnEmptyDependencies() {
    Dependencies deps = Dependencies.of(List.of());

    assertThat(deps.asList()).isEmpty();
    assertThat(deps.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("of(non-empty list) should sort by groupId:artifactId and be immutable")
  void of_nonEmptyList_shouldSortAndBeImmutable() {
    Dependency depZ = dep("org.zeta", "beta");
    Dependency depA = dep("org.alpha", "alpha");

    List<Dependency> raw = new ArrayList<>();
    raw.add(depZ);
    raw.add(depA);

    Dependencies deps = Dependencies.of(raw);

    var list = deps.asList();
    assertThat(list).hasSize(2);

    assertThat(list.get(0).coordinates().groupId().value()).isEqualTo("org.alpha");
    assertThat(list.get(0).coordinates().artifactId().value()).isEqualTo("alpha");
    assertThat(list.get(1).coordinates().groupId().value()).isEqualTo("org.zeta");
    assertThat(list.get(1).coordinates().artifactId().value()).isEqualTo("beta");

    raw.add(dep("org.extra", "extra"));

    var snapshot = deps.asList();
    assertThat(snapshot).hasSize(2);

    Dependency extraDep = dep("org.any", "any");
    assertThatThrownBy(() -> snapshot.add(extraDep))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("of(list with null item) should fail ITEM_REQUIRED")
  void of_listWithNullItem_shouldFailItemRequired() {
    Dependency d1 = dep("org.acme", "alpha");
    List<Dependency> raw = new ArrayList<>();
    raw.add(d1);
    raw.add(null);

    assertThatThrownBy(() -> Dependencies.of(raw))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.item.not.blank");
            });
  }

  @Test
  @DisplayName("of(list with duplicate coordinates) should fail DUPLICATE_COORDS")
  void of_listWithDuplicateCoords_shouldFailDuplicateCoords() {
    Dependency d1 = dep("org.acme", "common");
    Dependency d2 = dep("org.acme", "common");

    List<Dependency> raw = List.of(d1, d2);

    assertThatThrownBy(() -> Dependencies.of(raw))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("dependency.duplicate.coordinates");
              assertThat(dve.getArgs()).containsExactly("org.acme:common");
            });
  }
}
