package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.docs;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ProjectDocumentationPort;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.List;
import java.util.Map;

public class ProjectDocumentationAdapter extends AbstractSingleTemplateArtifactAdapter
    implements ProjectDocumentationPort {

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

  private final PomDependencyMapper pomDependencyMapper;

  public ProjectDocumentationAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      PomDependencyMapper pomDependencyMapper) {
    super(renderer, artifactDefinition);
    this.pomDependencyMapper = pomDependencyMapper;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.PROJECT_DOCUMENTATION;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getIdentity();
    TechStack stack = bp.getTechStack();
    SpringBootJvmTarget pt = (SpringBootJvmTarget) bp.getPlatformTarget();

    PackageName pkg = bp.getPackageName();
    Dependencies deps = bp.getDependencies();

    List<PomDependency> mappedDeps = pomDependencyMapper.from(deps);

    boolean hex = bp.getLayout().isHexagonal();

    return Map.ofEntries(
        entry(KEY_PROJECT_NAME, bp.getName().value()),
        entry(KEY_PROJECT_DESCRIPTION, bp.getDescription().value()),
        entry(KEY_GROUP_ID, id.groupId().value()),
        entry(KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(KEY_PACKAGE_NAME, pkg.value()),
        entry(KEY_BUILD_TOOL, stack.buildTool().key()),
        entry(KEY_LANGUAGE, stack.language().key()),
        entry(KEY_FRAMEWORK, stack.framework().key()),
        entry(KEY_JAVA_VERSION, pt.java().asString()),
        entry(KEY_SPRING_BOOT_VERSION, pt.springBoot().defaultVersion()),
        entry(KEY_DEPENDENCIES, mappedDeps),
        entry("hasHexSample", hex));
  }
}
