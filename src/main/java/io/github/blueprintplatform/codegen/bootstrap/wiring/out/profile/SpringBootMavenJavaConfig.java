package io.github.blueprintplatform.codegen.bootstrap.wiring.out.profile;

import io.github.blueprintplatform.codegen.adapter.error.exception.artifact.ArtifactKeyMismatchException;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.SpringBootMavenJavaArtifactsAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build.MavenPomBuildConfigurationAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.config.ApplicationYamlAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.doc.ProjectDocumentationAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.governance.ArchitectureGovernanceAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.ignore.GitIgnoreAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.sample.SampleCodeAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.MainSourceEntrypointAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.SourceLayoutAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.TestSourceEntrypointAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.wrapper.MavenWrapperBuildToolFilesAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.*;
import io.github.blueprintplatform.codegen.bootstrap.config.keys.ProfileKeys;
import io.github.blueprintplatform.codegen.bootstrap.config.registry.CodegenProfilesRegistry;
import io.github.blueprintplatform.codegen.bootstrap.error.exception.ProfileConfigurationException;
import io.github.blueprintplatform.codegen.bootstrap.mapper.ArtifactSpecMapper;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootMavenJavaConfig {

  private static final String PROFILE_KEY = ProfileKeys.SPRING_BOOT_MAVEN_JAVA;

  @Bean
  BuildConfigurationPort springBootMavenJavaMavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper artifactSpecMapper,
      PomDependencyMapper pomDependencyMapper) {

    ArtifactSpec spec =
        artifactSpecMapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.BUILD_CONFIG));

    return new MavenPomBuildConfigurationAdapter(renderer, spec, pomDependencyMapper);
  }

  @Bean
  BuildToolFilesPort springBootMavenJavaMavenWrapperBuildToolFilesAdapter(
      TemplateRenderer renderer, CodegenProfilesRegistry registry, ArtifactSpecMapper mapper) {

    ArtifactSpec spec =
        mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.BUILD_TOOL_FILES));

    return new MavenWrapperBuildToolFilesAdapter(renderer, spec);
  }

  @Bean
  IgnoreRulesPort springBootMavenJavaGitIgnoreAdapter(
      TemplateRenderer renderer, CodegenProfilesRegistry registry, ArtifactSpecMapper mapper) {

    ArtifactSpec spec =
        mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.IGNORE_RULES));
    return new GitIgnoreAdapter(renderer, spec);
  }

  @Bean
  SourceLayoutPort springBootMavenJavaSourceLayoutAdapter() {
    return new SourceLayoutAdapter();
  }

  @Bean
  ApplicationConfigurationPort springBootMavenJavaApplicationYamlAdapter(
      TemplateRenderer renderer, CodegenProfilesRegistry registry, ArtifactSpecMapper mapper) {

    ArtifactSpec spec = mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.APP_CONFIG));
    return new ApplicationYamlAdapter(renderer, spec);
  }

  @Bean
  MainSourceEntrypointPort springBootMavenJavaMainSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper mapper,
      StringCaseFormatter stringCaseFormatter) {

    ArtifactSpec spec =
        mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.MAIN_SOURCE_ENTRY_POINT));

    return new MainSourceEntrypointAdapter(renderer, spec, stringCaseFormatter);
  }

  @Bean
  TestSourceEntrypointPort springBootMavenJavaTestSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper mapper,
      StringCaseFormatter stringCaseFormatter) {

    ArtifactSpec spec =
        mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.TEST_SOURCE_ENTRY_POINT));

    return new TestSourceEntrypointAdapter(renderer, spec, stringCaseFormatter);
  }

  @Bean
  ArchitectureGovernancePort springBootMavenJavaArchitectureGovernanceAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper artifactSpecMapper,
      ClasspathTemplateScanner classpathTemplateScanner) {

    ArtifactSpec artifactSpec =
        artifactSpecMapper.from(
            registry.requireArtifact(PROFILE_KEY, ArtifactKey.ARCHITECTURE_GOVERNANCE));

    return new ArchitectureGovernanceAdapter(renderer, artifactSpec, classpathTemplateScanner);
  }

  @Bean
  SampleCodePort springBootMavenJavaSampleCodeAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper artifactSpecMapper,
      ClasspathTemplateScanner classpathTemplateScanner) {

    ArtifactSpec artifactSpec =
        artifactSpecMapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.SAMPLE_CODE));

    return new SampleCodeAdapter(renderer, artifactSpec, classpathTemplateScanner);
  }

  @Bean
  ProjectDocumentationPort springBootMavenJavaProjectDocumentationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesRegistry registry,
      ArtifactSpecMapper mapper,
      PomDependencyMapper pomDependencyMapper) {

    ArtifactSpec spec =
        mapper.from(registry.requireArtifact(PROFILE_KEY, ArtifactKey.PROJECT_DOCUMENTATION));

    return new ProjectDocumentationAdapter(renderer, spec, pomDependencyMapper);
  }

  @Bean
  Map<ArtifactKey, ArtifactPort> springBootMavenJavaArtifactRegistry(
      BuildConfigurationPort springBootMavenJavaMavenPomBuildConfigurationAdapter,
      BuildToolFilesPort springBootMavenJavaMavenWrapperBuildToolFilesAdapter,
      IgnoreRulesPort springBootMavenJavaGitIgnoreAdapter,
      SourceLayoutPort springBootMavenJavaSourceLayoutAdapter,
      ApplicationConfigurationPort springBootMavenJavaApplicationYamlAdapter,
      MainSourceEntrypointPort springBootMavenJavaMainSourceEntrypointAdapter,
      TestSourceEntrypointPort springBootMavenJavaTestSourceEntrypointAdapter,
      ArchitectureGovernancePort springBootMavenJavaArchitectureGovernanceAdapter,
      SampleCodePort springBootMavenJavaSampleCodeAdapter,
      ProjectDocumentationPort springBootMavenJavaProjectDocumentationAdapter) {

    Map<ArtifactKey, ArtifactPort> registry = new EnumMap<>(ArtifactKey.class);
    registry.put(ArtifactKey.BUILD_CONFIG, springBootMavenJavaMavenPomBuildConfigurationAdapter);
    registry.put(
        ArtifactKey.BUILD_TOOL_FILES, springBootMavenJavaMavenWrapperBuildToolFilesAdapter);
    registry.put(ArtifactKey.IGNORE_RULES, springBootMavenJavaGitIgnoreAdapter);
    registry.put(ArtifactKey.SOURCE_LAYOUT, springBootMavenJavaSourceLayoutAdapter);
    registry.put(ArtifactKey.APP_CONFIG, springBootMavenJavaApplicationYamlAdapter);
    registry.put(
        ArtifactKey.MAIN_SOURCE_ENTRY_POINT, springBootMavenJavaMainSourceEntrypointAdapter);
    registry.put(
        ArtifactKey.TEST_SOURCE_ENTRY_POINT, springBootMavenJavaTestSourceEntrypointAdapter);
    registry.put(
        ArtifactKey.ARCHITECTURE_GOVERNANCE, springBootMavenJavaArchitectureGovernanceAdapter);
    registry.put(ArtifactKey.SAMPLE_CODE, springBootMavenJavaSampleCodeAdapter);
    registry.put(ArtifactKey.PROJECT_DOCUMENTATION, springBootMavenJavaProjectDocumentationAdapter);
    return Collections.unmodifiableMap(registry);
  }

  @Bean
  ProjectArtifactsPort springBootMavenJavaArtifactsAdapter(
      CodegenProfilesRegistry codegenProfilesProperties,
      Map<ArtifactKey, ArtifactPort> springBootMavenJavaArtifactRegistry) {

    var profile = codegenProfilesProperties.requireProfile(PROFILE_KEY);
    var orderedArtifactKeys = profile.orderedArtifactKeys();

    List<ArtifactPort> ordered =
        orderedArtifactKeys.stream()
            .map(
                key -> {
                  ArtifactPort port = springBootMavenJavaArtifactRegistry.get(key);
                  if (port == null) {
                    throw new ProfileConfigurationException(
                        "bootstrap.requireArtifact.not.found", key.key(), PROFILE_KEY);
                  }
                  if (!port.artifactKey().equals(key)) {
                    throw new ArtifactKeyMismatchException(key, port.artifactKey().key());
                  }
                  return port;
                })
            .toList();

    return new SpringBootMavenJavaArtifactsAdapter(ordered);
  }
}
