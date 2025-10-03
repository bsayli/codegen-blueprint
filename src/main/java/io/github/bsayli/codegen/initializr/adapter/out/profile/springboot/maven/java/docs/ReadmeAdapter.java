package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.docs;

import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.adapter.out.spi.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ReadmePort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactProperties;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.pkg.PackageName;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.PlatformTarget;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class ReadmeAdapter implements ReadmePort, ArtifactGenerator {

  public static final String PROFILE_KEY = "springboot-maven-java";
  private static final int ORDER = 90;
  private static final String NAME = "readme";
  // Model keys
  private static final String KEY_PROJECT_NAME = "projectName";
  private static final String KEY_PROJECT_DESCRIPTION = "projectDescription";
  private static final String KEY_GROUP_ID = "groupId";
  private static final String KEY_ARTIFACT_ID = "artifactId";
  private static final String KEY_PACKAGE_NAME = "packageName";
  private static final String KEY_BUILD_TOOL = "buildTool";
  private static final String KEY_LANGUAGE = "language";
  private static final String KEY_FRAMEWORK = "framework";
  private static final String KEY_JAVA_VERSION = "javaVersion";
  private static final String KEY_SPRING_BOOT_VERSION = "springBootVersion";
  private static final String KEY_DEPENDENCIES = "dependencies";

  private final TemplateRenderer renderer;
  private final CodegenProfilesProperties profiles;
  private final String profileKey;

  public ReadmeAdapter(TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    this(renderer, profiles, PROFILE_KEY);
  }

  public ReadmeAdapter(
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
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return List.of(generate(blueprint));
  }

  @Override
  public boolean supports(ProjectBlueprint bp) {
    ArtifactProperties cfg = cfg();
    return cfg.enabled();
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
    BuildOptions bo = bp.getBuildOptions();
    PlatformTarget pt = bp.getPlatformTarget();
    PackageName pn = bp.getPackageName();
    Dependencies deps = bp.getDependencies();

    return Map.ofEntries(
        entry(KEY_PROJECT_NAME, bp.getName().value()),
        entry(KEY_PROJECT_DESCRIPTION, bp.getDescription().value()),
        entry(KEY_GROUP_ID, id.groupId().value()),
        entry(KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(KEY_PACKAGE_NAME, pn.value()),
        entry(KEY_BUILD_TOOL, bo.buildTool().name()),
        entry(KEY_LANGUAGE, bo.language().name()),
        entry(KEY_FRAMEWORK, bo.framework().name()),
        entry(KEY_JAVA_VERSION, pt.java().asString()),
        entry(KEY_SPRING_BOOT_VERSION, pt.springBoot().value()),
        entry(KEY_DEPENDENCIES, deps.asList()));
  }
}
