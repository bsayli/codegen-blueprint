package io.github.bsayli.codegen.initializr.domain.policy.tech;

import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_21;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_25;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_4_10;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_5_6;
import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
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

  public static void ensureCompatible(BuildOptions options, PlatformTarget target) {
    if (options == null || target == null) {
      throw new DomainViolationException(TARGET_MISSING);
    }

    if (options.framework() != Framework.SPRING_BOOT
        || options.language() != Language.JAVA
        || options.buildTool() != BuildTool.MAVEN) {
      throw new DomainViolationException(
          OPTIONS_UNSUPPORTED, options.framework(), options.language(), options.buildTool());
    }

    var allowed = SPRINGBOOT_JAVA_SUPPORT.getOrDefault(target.springBoot(), Set.of());
    if (!allowed.contains(target.java())) {
      throw new DomainViolationException(
          TARGET_INCOMPATIBLE, target.springBoot().value(), target.java().asString());
    }
  }

  public static Set<JavaVersion> allowedJavaFor(SpringBootVersion boot) {
    return SPRINGBOOT_JAVA_SUPPORT.getOrDefault(boot, Set.of());
  }

  public static List<PlatformTarget> allSupportedTargets() {
    List<PlatformTarget> list = new ArrayList<>();
    for (var e : SPRINGBOOT_JAVA_SUPPORT.entrySet()) {
      for (var j : e.getValue()) {
        list.add(new PlatformTarget(j, e.getKey()));
      }
    }
    return List.copyOf(list);
  }
}
