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
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: ProjectBlueprintFactory")
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

  private static BuildOptions buildOptions() {
    return new BuildOptions(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);
  }

  private static PlatformTarget target() {
    return new PlatformTarget(JavaVersion.JAVA_21, SpringBootVersion.V3_5_6);
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
      "of(identity, name, desc, package, options, target, dependencies) should create blueprint with same references")
  void of_withDependenciesObject_shouldCreateBlueprint() {
    ProjectIdentity identity = identity();
    ProjectName name = name();
    ProjectDescription description = description();
    PackageName packageName = pkg();
    BuildOptions options = buildOptions();
    PlatformTarget target = target();
    Dependencies dependencies = dependencies();

    ProjectBlueprint bp =
        ProjectBlueprintFactory.of(
            identity, name, description, packageName, options, target, dependencies);

    assertThat(bp.getIdentity()).isSameAs(identity);
    assertThat(bp.getName()).isSameAs(name);
    assertThat(bp.getDescription()).isSameAs(description);
    assertThat(bp.getPackageName()).isSameAs(packageName);
    assertThat(bp.getBuildOptions()).isSameAs(options);
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
            identity(), name(), description(), pkg(), buildOptions(), target(), List.of(d1, d2));

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
            identity(), name(), description(), pkg(), buildOptions(), target(), d1, d2);

    assertThat(bp.getDependencies()).isNotNull();
    assertThat(bp.getDependencies().asList()).hasSize(2);
  }

  @Test
  @DisplayName("null identity should fail with project.identity.not.blank")
  void nullIdentity_shouldFailIdentityRequired() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    null, name(), description(), pkg(), buildOptions(), target(), dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("project.identity.not.blank"));
  }

  @Test
  @DisplayName("null project name should fail with project.name.not.blank")
  void nullProjectName_shouldFailNotBlankRule() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(),
                    null,
                    description(),
                    pkg(),
                    buildOptions(),
                    target(),
                    dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("project.name.not.blank"));
  }

  @Test
  @DisplayName("null package name should fail with project.package-name.not.blank")
  void nullPackageName_shouldFailNotBlankRule() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(),
                    name(),
                    description(),
                    null,
                    buildOptions(),
                    target(),
                    dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("project.package-name.not.blank"));
  }

  @Test
  @DisplayName("null build options should fail with project.build-options.not.blank")
  void nullBuildOptions_shouldFailBuildOptionsRequired() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(), name(), description(), pkg(), null, target(), dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("project.build-options.not.blank"));
  }

  @Test
  @DisplayName("null platform target should fail with platform.target.not.blank")
  void nullPlatformTarget_shouldFailTargetRequired() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(), name(), description(), pkg(), buildOptions(), null, dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.not.blank"));
  }

  @Test
  @DisplayName("null dependencies should fail with dependency.list.not.blank")
  void nullDependencies_shouldFailDependenciesRequired() {
    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(),
                    name(),
                    description(),
                    pkg(),
                    buildOptions(),
                    target(),
                    (Dependencies) null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("dependency.list.not.blank"));
  }

  @Test
  @DisplayName("incompatible platform target should delegate to CompatibilityPolicy and fail")
  void incompatiblePlatformTarget_shouldFailCompatibility() {
    BuildOptions options = buildOptions();
    PlatformTarget incompatible =
        new PlatformTarget(JavaVersion.JAVA_25, SpringBootVersion.V3_4_10);

    assertThatThrownBy(
            () ->
                ProjectBlueprintFactory.of(
                    identity(),
                    name(),
                    description(),
                    pkg(),
                    options,
                    incompatible,
                    dependencies()))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("platform.target.incompatible"));
  }
}
