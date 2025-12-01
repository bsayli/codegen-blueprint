package io.github.bsayli.codegen.initializr.domain.policy.tech;

import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_21;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_25;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_4_10;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_5_6;
import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CompatibilityPolicy {

  private static final ErrorCode TARGET_MISSING = () -> "platform.target.missing";
  private static final ErrorCode OPTIONS_UNSUPPORTED = () -> "platform.target.unsupported.options";
  private static final ErrorCode TARGET_INCOMPATIBLE = () -> "platform.target.incompatible";

  private static final Map<SpringBootVersion, Set<JavaVersion>> SPRINGBOOT_JAVA_SUPPORT =
      Map.ofEntries(
          entry(V3_5_6, EnumSet.of(JAVA_21, JAVA_25)), entry(V3_4_10, EnumSet.of(JAVA_21)));

  private CompatibilityPolicy() {}

  public static void ensureCompatible(TechStack techStack, PlatformTarget target) {
    if (techStack == null || target == null) {
      throw new DomainViolationException(TARGET_MISSING);
    }

    if (techStack.framework() != Framework.SPRING_BOOT
        || techStack.language() != Language.JAVA
        || techStack.buildTool() != BuildTool.MAVEN) {
      throw new DomainViolationException(
          OPTIONS_UNSUPPORTED, techStack.framework(), techStack.language(), techStack.buildTool());
    }

    if (!(target instanceof SpringBootJvmTarget(JavaVersion java, SpringBootVersion springBoot))) {
      throw new DomainViolationException(
          TARGET_INCOMPATIBLE, "SPRING_BOOT", target.getClass().getSimpleName());
    }

    var allowed = SPRINGBOOT_JAVA_SUPPORT.getOrDefault(springBoot, Set.of());
    if (!allowed.contains(java)) {
      throw new DomainViolationException(TARGET_INCOMPATIBLE, springBoot.value(), java.asString());
    }
  }

  public static List<PlatformTarget> allSupportedTargets() {
    List<PlatformTarget> list = new ArrayList<>();
    for (var e : SPRINGBOOT_JAVA_SUPPORT.entrySet()) {
      for (var j : e.getValue()) {
        list.add(new SpringBootJvmTarget(j, e.getKey()));
      }
    }
    return List.copyOf(list);
  }
}
