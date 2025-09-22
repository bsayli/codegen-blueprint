// domain/policy/tech/PlatformTargetSelector.java
package io.github.bsayli.codegen.initializr.domain.policy.tech;

import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_21;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion.JAVA_25;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_4_10;
import static io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion.V3_5_6;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.JavaVersion;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootVersion;
import java.util.Comparator;
import java.util.List;

public final class PlatformTargetSelector {

  private static final List<SpringBootVersion> BOOT_PRIORITY = List.of(V3_5_6, V3_4_10);
  private static final List<JavaVersion> JAVA_PRIORITY = List.of(JAVA_21, JAVA_25);

  private PlatformTargetSelector() {}

  @SuppressWarnings("unused")
  public static PlatformTarget selectDefaultFor(BuildOptions options) {
    var candidates = CompatibilityPolicy.allSupportedTargets();
    return candidates.stream()
        .min(
            Comparator.comparing((PlatformTarget t) -> BOOT_PRIORITY.indexOf(t.springBoot()))
                .thenComparing(t -> JAVA_PRIORITY.indexOf(t.java())))
        .orElse(new PlatformTarget(JAVA_21, V3_5_6));
  }

  @SuppressWarnings("unused")
  public static PlatformTarget selectOrDefault(
      BuildOptions options, JavaVersion preferredJava, SpringBootVersion preferredBoot) {
    var requested = new PlatformTarget(preferredJava, preferredBoot);
    try {
      CompatibilityPolicy.ensureCompatible(options, requested);
      return requested;
    } catch (RuntimeException ignored) {
      return selectDefaultFor(options);
    }
  }

  @SuppressWarnings("unused")
  public static List<PlatformTarget> supportedTargetsFor(BuildOptions options) {
    return CompatibilityPolicy.allSupportedTargets();
  }
}
