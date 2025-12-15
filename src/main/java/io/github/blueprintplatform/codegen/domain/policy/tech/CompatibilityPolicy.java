package io.github.blueprintplatform.codegen.domain.policy.tech;

import static io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion.JAVA_21;
import static io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion.JAVA_25;
import static io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion.V3_4;
import static io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion.V3_5;
import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.model.value.tech.PlatformSpec;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CompatibilityPolicy {

  private static final ErrorCode TARGET_MISSING = () -> "platform.target.missing";
  private static final ErrorCode OPTIONS_UNSUPPORTED = () -> "platform.target.unsupported.options";
  private static final ErrorCode TARGET_INCOMPATIBLE = () -> "platform.target.incompatible";

  private static final Map<SpringBootVersion, Set<JavaVersion>> SPRINGBOOT_JAVA_SUPPORT =
      Map.ofEntries(entry(V3_5, EnumSet.of(JAVA_21, JAVA_25)), entry(V3_4, EnumSet.of(JAVA_21)));

  private CompatibilityPolicy() {}

  public static void ensureCompatible(PlatformSpec platform) {
    if (platform == null) {
      throw new DomainViolationException(TARGET_MISSING);
    }
    ensureCompatible(platform.techStack(), platform.platformTarget());
  }

  public static void ensureCompatible(TechStack techStack, PlatformTarget target) {
    requirePresent(techStack, target);
    requireSupportedStack(techStack);

    SpringBootJvmTarget jvmTarget = requireSpringBootJvmTarget(target);

    requireSpringBootJavaCompatible(jvmTarget.springBoot(), jvmTarget.java());
  }

  public static List<PlatformTarget> allSupportedTargets() {
    return SPRINGBOOT_JAVA_SUPPORT.entrySet().stream()
        .flatMap(e -> e.getValue().stream().map(j -> new SpringBootJvmTarget(j, e.getKey())))
        .map(PlatformTarget.class::cast)
        .toList();
  }

  private static void requirePresent(TechStack techStack, PlatformTarget target) {
    if (techStack == null || target == null) {
      throw new DomainViolationException(TARGET_MISSING);
    }
  }

  private static void requireSupportedStack(TechStack techStack) {
    if (techStack.framework() != Framework.SPRING_BOOT
        || techStack.language() != Language.JAVA
        || techStack.buildTool() != BuildTool.MAVEN) {
      throw new DomainViolationException(
          OPTIONS_UNSUPPORTED, techStack.framework(), techStack.language(), techStack.buildTool());
    }
  }

  private static SpringBootJvmTarget requireSpringBootJvmTarget(PlatformTarget target) {
    if (target instanceof SpringBootJvmTarget jvm) {
      return jvm;
    }
    throw new DomainViolationException(
        TARGET_INCOMPATIBLE, Framework.SPRING_BOOT.key(), target.getClass().getSimpleName());
  }

  private static void requireSpringBootJavaCompatible(
      SpringBootVersion springBoot, JavaVersion java) {
    Set<JavaVersion> allowed = SPRINGBOOT_JAVA_SUPPORT.getOrDefault(springBoot, Set.of());
    if (!allowed.contains(java)) {
      throw new DomainViolationException(
          TARGET_INCOMPATIBLE, springBoot.defaultVersion(), java.asString());
    }
  }
}
