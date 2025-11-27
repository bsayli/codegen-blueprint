package io.github.bsayli.codegen.initializr.domain.factory;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;
import static io.github.bsayli.codegen.initializr.domain.error.code.Field.PACKAGE_NAME;
import static io.github.bsayli.codegen.initializr.domain.error.code.Field.PROJECT_NAME;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.NOT_BLANK;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectDescription;
import io.github.bsayli.codegen.initializr.domain.model.value.naming.ProjectName;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import io.github.bsayli.codegen.initializr.domain.policy.tech.CompatibilityPolicy;
import java.util.Arrays;
import java.util.List;

public final class ProjectBlueprintFactory {

  private static final ErrorCode IDENTITY_REQUIRED = () -> "project.identity.not.blank";
  private static final ErrorCode TECH_STACK_REQUIRED = () -> "project.tech-stack.not.blank";
  private static final ErrorCode TARGET_REQUIRED = () -> "platform.target.not.blank";
  private static final ErrorCode DEPENDENCIES_REQUIRED = () -> "dependency.list.not.blank";

  private ProjectBlueprintFactory() {}

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      Dependencies dependencies) {

    ensureNotNull(identity, IDENTITY_REQUIRED);
    ensureNotNull(name, compose(PROJECT_NAME, NOT_BLANK));
    ensureNotNull(packageName, compose(PACKAGE_NAME, NOT_BLANK));
    ensureNotNull(techStack, TECH_STACK_REQUIRED);
    ensureNotNull(platformTarget, TARGET_REQUIRED);
    ensureNotNull(dependencies, DEPENDENCIES_REQUIRED);

    CompatibilityPolicy.ensureCompatible(techStack, platformTarget);

    return new ProjectBlueprint(
        identity, name, description, packageName, techStack, platformTarget, dependencies);
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      List<Dependency> dependencies) {
    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        platformTarget,
        Dependencies.of(dependencies));
  }

  public static ProjectBlueprint of(
      ProjectIdentity identity,
      ProjectName name,
      ProjectDescription description,
      PackageName packageName,
      TechStack techStack,
      PlatformTarget platformTarget,
      Dependency... deps) {
    return of(
        identity,
        name,
        description,
        packageName,
        techStack,
        platformTarget,
        Dependencies.of(Arrays.asList(deps)));
  }

  private static void ensureNotNull(Object value, ErrorCode code) {
    if (value == null) throw new DomainViolationException(code);
  }
}
