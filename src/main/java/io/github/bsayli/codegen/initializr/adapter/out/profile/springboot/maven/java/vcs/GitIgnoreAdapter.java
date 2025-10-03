package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.vcs;

import io.github.bsayli.codegen.initializr.adapter.out.spi.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.GitIgnorePort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactProperties;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class GitIgnoreAdapter implements GitIgnorePort, ArtifactGenerator {

  public static final String PROFILE_KEY = "springboot-maven-java";
  private static final int ORDER = 20;
  private static final String NAME = "gitignore";
  private static final String KEY_IGNORE_LIST = "ignoreList";
  private final TemplateRenderer renderer;
  private final CodegenProfilesProperties profiles;
  private final String profileKey;

  public GitIgnoreAdapter(TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    this(renderer, profiles, PROFILE_KEY);
  }

  public GitIgnoreAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles, String profileKey) {
    this.renderer = renderer;
    this.profiles = profiles;
    this.profileKey = profileKey;
  }

  @Override
  public GeneratedFile generate(BuildOptions buildOptions) {
    ArtifactProperties cfg = cfg();
    Path outPath = Path.of(cfg.outputPath());
    String template = cfg.template();
    Map<String, Object> model = buildModel(buildOptions);
    return renderer.renderUtf8(outPath, template, model);
  }

  @Override
  public Iterable<? extends GeneratedFile> generateFiles(ProjectBlueprint bp) {
    return List.of(generate(bp.getBuildOptions()));
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

  @SuppressWarnings("unused")
  private Map<String, Object> buildModel(BuildOptions buildOptions) {
    return Map.of(KEY_IGNORE_LIST, List.of());
  }
}
