package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.config;

import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.adapter.out.spi.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ConfigFilesPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactProperties;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class ApplicationYamlAdapter implements ConfigFilesPort, ArtifactGenerator {

  public static final String PROFILE_KEY = "springboot-maven-java";
  private static final int ORDER = 50;
  private static final String NAME = "application-yaml";
  private static final String KEY_FRAMEWORK = "framework";
  private static final String KEY_BUILD_TOOL = "buildTool";
  private static final String KEY_LANGUAGE = "language";

  private final TemplateRenderer renderer;
  private final CodegenProfilesProperties profiles;
  private final String profileKey;

  public ApplicationYamlAdapter(TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    this(renderer, profiles, PROFILE_KEY);
  }

  public ApplicationYamlAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles, String profileKey) {
    this.renderer = renderer;
    this.profiles = profiles;
    this.profileKey = profileKey;
  }

  @Override
  public Iterable<? extends GeneratedFile> generate(BuildOptions options) {
    ArtifactProperties cfg = cfg();
    Path outPath = Path.of(cfg.outputPath());
    String template = cfg.template();
    Map<String, Object> model = buildModel(options);
    return List.of(renderer.renderUtf8(outPath, template, model));
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint blueprint) {
    return generate(blueprint.getBuildOptions());
  }

  @Override
  public boolean supports(ProjectBlueprint blueprint) {
    return cfg().enabled();
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

  private Map<String, Object> buildModel(BuildOptions options) {
    return Map.ofEntries(
        entry(KEY_FRAMEWORK, options.framework().name()),
        entry(KEY_BUILD_TOOL, options.buildTool().name()),
        entry(KEY_LANGUAGE, options.language().name()));
  }
}
