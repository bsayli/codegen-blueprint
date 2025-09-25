package io.github.bsayli.codegen.initializr.adapter.out.generator.vcs;

import io.github.bsayli.codegen.initializr.adapter.out.generator.ArtifactGenerator;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.GitIgnorePort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactProperties;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenArtifactsProperties;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class GitIgnoreAdapter implements GitIgnorePort, ArtifactGenerator {

  private static final int ORDER = 20;
  private static final String NAME = "gitignore";

  private static final String KEY_IGNORE_LIST = "ignoreList";

  private final TemplateRenderer renderer;
  private final ArtifactProperties cfg;

  public GitIgnoreAdapter(TemplateRenderer renderer, CodegenArtifactsProperties props) {
    this.renderer = renderer;
    this.cfg = props.gitignore();
  }

  @Override
  public boolean supports(ProjectBlueprint bp) {
    return cfg.enabled();
  }

  @Override
  public GeneratedFile generate(BuildOptions buildOptions) {
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
  public int order() {
    return ORDER;
  }

  @Override
  public String name() {
    return NAME;
  }

  @SuppressWarnings("unused")
  private Map<String, Object> buildModel(BuildOptions buildOptions) {
    return Map.of(KEY_IGNORE_LIST, List.of());
  }
}
