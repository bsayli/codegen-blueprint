package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.build;

import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.adapter.out.spi.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.MavenPomPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactProperties;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MavenPomAdapter implements MavenPomPort, ArtifactGenerator {

  public static final String PROFILE_KEY = "springboot-maven-java";
  private static final int ORDER = 10;
  private static final String NAME = "maven-pom";
  private static final String KEY_GROUP_ID = "groupId";
  private static final String KEY_ARTIFACT_ID = "artifactId";
  private static final String KEY_JAVA_VERSION = "javaVersion";
  private static final String KEY_SPRING_BOOT_VER = "springBootVersion";
  private static final String KEY_DEPENDENCIES = "dependencies";
  private static final String KEY_PROJECT_NAME = "projectName";
  private static final String KEY_PROJECT_DESCRIPTION = "projectDescription";

  private static final Map<String, String> CORE_STARTER =
      Map.of(KEY_GROUP_ID, "org.springframework.boot", KEY_ARTIFACT_ID, "spring-boot-starter");

  private static final Map<String, String> TEST_STARTER =
      Map.of(
          KEY_GROUP_ID,
          "org.springframework.boot",
          KEY_ARTIFACT_ID,
          "spring-boot-starter-test",
          "scope",
          "test");

  private final TemplateRenderer renderer;
  private final CodegenProfilesProperties profiles;
  private final String profileKey;

  public MavenPomAdapter(TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    this(renderer, profiles, PROFILE_KEY);
  }

  public MavenPomAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles, String profileKey) {
    this.renderer = renderer;
    this.profiles = profiles;
    this.profileKey = profileKey;
  }

  @Override
  public GeneratedFile generate(ProjectBlueprint blueprint) {
    ArtifactProperties cfg = cfg();
    Path outPath = Path.of(cfg.outputPath());
    String template = cfg.template();
    Map<String, Object> model = buildModel(blueprint);
    return renderer.renderUtf8(outPath, template, model);
  }

  @Override
  public boolean supports(ProjectBlueprint bp) {
    ArtifactProperties cfg = cfg();
    return cfg.enabled();
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return List.of(generate(blueprint));
  }

  @Override
  public int order() {
    return ORDER;
  }

  @Override
  public String name() {
    return NAME;
  }

  private ArtifactProperties cfg() {
    return profiles.artifact(profileKey, NAME);
  }

  private Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getIdentity();
    PlatformTarget pt = bp.getPlatformTarget();

    List<Map<String, String>> dependencies = new ArrayList<>();
    dependencies.add(CORE_STARTER);
    dependencies.addAll(mapUserDependencies(bp.getDependencies()));
    dependencies.add(TEST_STARTER);

    return Map.ofEntries(
        entry(KEY_GROUP_ID, id.groupId().value()),
        entry(KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(KEY_JAVA_VERSION, pt.java().asString()),
        entry(KEY_SPRING_BOOT_VER, pt.springBoot().value()),
        entry(KEY_PROJECT_NAME, bp.getName().value()),
        entry(KEY_PROJECT_DESCRIPTION, bp.getDescription().value()),
        entry(KEY_DEPENDENCIES, dependencies));
  }

  private List<Map<String, String>> mapUserDependencies(Dependencies userDependencies) {
    if (userDependencies == null || userDependencies.isEmpty()) return List.of();
    List<Map<String, String>> list = new ArrayList<>(userDependencies.asList().size());
    for (Dependency d : userDependencies.asList()) {
      list.add(toMap(d));
    }
    return list;
  }

  private Map<String, String> toMap(Dependency d) {
    Map<String, String> m = new LinkedHashMap<>();
    m.put(KEY_GROUP_ID, d.coordinates().groupId().value());
    m.put(KEY_ARTIFACT_ID, d.coordinates().artifactId().value());
    if (d.version() != null && !d.version().value().isBlank()) {
      m.put("version", d.version().value());
    }
    if (d.scope() != null && !d.scope().value().isBlank()) {
      m.put("scope", d.scope().value());
    }
    return m;
  }
}
