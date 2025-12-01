package io.github.bsayli.codegen.initializr.domain.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
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
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class ProjectBlueprintFactoryTest {

  private static ProjectIdentity identity() {
    return new ProjectIdentity(new GroupId("com.example"), new ArtifactId("demo-app"));
  }

  private static ProjectName name() {
    return new ProjectName("demo-app");
  }

  private static ProjectDescription description() {
    return new ProjectDescription("simple demo project");
  }

  private static PackageName pkg() {
    return new PackageName("com.example.demo");
  }

  private static TechStack techStack() {
    return new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
  }

  private static PlatformTarget target() {
    return new SpringBootJvmTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);
  }

  private static Dependencies dependencies() {
    Dependency d = dep("org.acme", "alpha");
    return Dependencies.of(List.of(d));
  }

  private static Dependency dep(String groupId, String artifactId) {
    return new Dependency(
        new DependencyCoordinates(new GroupId(groupId), new ArtifactId(artifactId)), null, null);
  }

  @Test
  @DisplayName(
      "of(identity, name, desc, package, stack, target, dependencies) should create blueprint with same references")
  void of_withDependenciesObject_shouldCreateBlueprint() {
    ProjectIdentity identity = identity();
    ProjectName name = name();
    ProjectDescription description = description();
    PackageName packageName = pkg();
    TechStack stack = techStack();
    PlatformTarget target = target();
    Dependencies dependencies = dependencies();

    ProjectBlueprint bp =
        ProjectBlueprintFactory.of(
            identity, name, description, packageName, stack, target, dependencies);

    assertThat(bp.getIdentity()).isSameAs(identity);
    assertThat(bp.getName()).isSameAs(name);
    assertThat(bp.getDescription()).isSameAs(description);
    assertThat(bp.getPackageName()).isSameAs(packageName);
    assertThat(bp.getTechStack()).isSameAs(stack);
    assertThat(bp.getPlatformTarget()).isSameAs(target);
    assertThat(bp.getDependencies()).isSameAs(dependencies);
  }

  @Test
  @DisplayName("of(..., List<Dependency>) should wrap list into Dependencies")
  void of_withDependencyList_shouldWrapIntoDependencies() {
    var d1 = dep("org.acme", "alpha");
    var d2 = dep("org.example", "beta");

    ProjectBlueprint bp =
        ProjectBlueprintFactory.of(
            identity(), name(), description(), pkg(), techStack(), target(), List.of(d1, d2));

    assertThat(bp.getDependencies()).isNotNull();
    assertThat(bp.getDependencies().asList()).hasSize(2);
  }

  @Test
  @DisplayName("of(..., Dependency...) should wrap varargs into Dependencies")
  void of_withVarargs_shouldWrapIntoDependencies() {
    var d1 = dep("org.acme", "alpha");
    var d2 = dep("org.example", "beta");

    ProjectBlueprint bp =
        ProjectBlueprintFactory.of(
            identity(), name(), description(), pkg(), techStack(), target(), d1, d2);

    assertThat(bp.getDependencies()).isNotNull();
    assertThat(bp.getDependencies().asList()).hasSize(2);
  }

  @Test
  @DisplayName("null tech stack should fail via CompatibilityPolicy with platform.target.missing")
  void nullTechStack_shouldFailPlatformTargetMissing() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(), name(), description(), pkg(), null, target(), dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));
  }

  @Test
  @DisplayName(
      "null platform target should fail via CompatibilityPolicy with platform.target.missing")
  void nullPlatformTarget_shouldFailPlatformTargetMissing() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(), name(), description(), pkg(), techStack(), null, dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.missing"));
  }

  @Test
  @DisplayName("incompatible platform target should delegate to CompatibilityPolicy and fail")
  void incompatiblePlatformTarget_shouldFailCompatibility() {
    TechStack stack = techStack();
    PlatformTarget incompatible =
        new SpringBootJvmTarget(JavaVersion.JAVA_25, SpringBootVersion.V3_4_10);

    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(), name(), description(), pkg(), stack, incompatible, dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.incompatible"));
  }
}
