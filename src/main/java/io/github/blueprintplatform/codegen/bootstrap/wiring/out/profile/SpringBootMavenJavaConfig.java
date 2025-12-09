package io.github.blueprintplatform.codegen.bootstrap.wiring.out.profile;

import io.github.blueprintplatform.codegen.adapter.error.exception.ArtifactKeyMismatchException;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.profile.ProfileType;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.SpringBootMavenJavaArtifactsAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build.MavenPomBuildConfigurationAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.config.ApplicationYamlAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.doc.ProjectDocumentationAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.ignore.GitIgnoreAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.sample.SampleCodeAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.MainSourceEntrypointAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.SourceLayoutAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.source.TestSourceEntrypointAdapter;
import io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.wrapper.MavenWrapperBuildToolFilesAdapter;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.*;
import io.github.blueprintplatform.codegen.bootstrap.config.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.CodegenProfilesProperties;
import io.github.blueprintplatform.codegen.bootstrap.error.exception.ProfileConfigurationException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootMavenJavaConfig {

  @Bean
  BuildConfigurationPort springBootMavenJavaMavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.BUILD_CONFIG);
    return new MavenPomBuildConfigurationAdapter(renderer, props, pomDependencyMapper);
  }

  @Bean
  BuildToolFilesPort springBootMavenJavaMavenWrapperBuildToolFilesAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.BUILD_TOOL_METADATA);
    return new MavenWrapperBuildToolFilesAdapter(renderer, props);
  }

  @Bean
  IgnoreRulesPort springBootMavenJavaGitIgnoreAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.IGNORE_RULES);
    return new GitIgnoreAdapter(renderer, props);
  }

  @Bean
  SourceLayoutPort springBootMavenJavaSourceLayoutAdapter() {
    return new SourceLayoutAdapter();
  }

  @Bean
  ApplicationConfigurationPort springBootMavenJavaApplicationYamlAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.APP_CONFIG);
    return new ApplicationYamlAdapter(renderer, props);
  }

  @Bean
  MainSourceEntrypointPort springBootMavenJavaMainSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.MAIN_SOURCE_ENTRY_POINT);
    return new MainSourceEntrypointAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  TestSourceEntrypointPort springBootMavenJavaTestSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.TEST_SOURCE_ENTRY_POINT);
    return new TestSourceEntrypointAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  SampleCodePort springBootMavenJavaSampleCodeAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.PROJECT_DOCUMENTATION);
    return new SampleCodeAdapter(renderer, props, profiles.samples());
  }

  @Bean
  ProjectDocumentationPort springBootMavenJavaProjectDocumentationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.PROJECT_DOCUMENTATION);
    return new ProjectDocumentationAdapter(renderer, props, pomDependencyMapper);
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
      SampleCodePort springBootMavenJavaSampleCodeAdapter,
      ProjectDocumentationPort springBootMavenJavaProjectDocumentationAdapter) {

    Map<ArtifactKey, ArtifactPort> registry = new EnumMap<>(ArtifactKey.class);
    registry.put(ArtifactKey.BUILD_CONFIG, springBootMavenJavaMavenPomBuildConfigurationAdapter);
    registry.put(
        ArtifactKey.BUILD_TOOL_METADATA, springBootMavenJavaMavenWrapperBuildToolFilesAdapter);
    registry.put(ArtifactKey.IGNORE_RULES, springBootMavenJavaGitIgnoreAdapter);
    registry.put(ArtifactKey.SOURCE_LAYOUT, springBootMavenJavaSourceLayoutAdapter);
    registry.put(ArtifactKey.APP_CONFIG, springBootMavenJavaApplicationYamlAdapter);
    registry.put(
        ArtifactKey.MAIN_SOURCE_ENTRY_POINT, springBootMavenJavaMainSourceEntrypointAdapter);
    registry.put(
        ArtifactKey.TEST_SOURCE_ENTRY_POINT, springBootMavenJavaTestSourceEntrypointAdapter);
    registry.put(ArtifactKey.SAMPLE_CODE, springBootMavenJavaSampleCodeAdapter);
    registry.put(ArtifactKey.PROJECT_DOCUMENTATION, springBootMavenJavaProjectDocumentationAdapter);
    return Collections.unmodifiableMap(registry);
  }

  @Bean
  ProjectArtifactsPort springBootMavenJavaArtifactsAdapter(
      CodegenProfilesProperties codegenProfilesProperties,
      Map<ArtifactKey, ArtifactPort> springBootMavenJavaArtifactRegistry) {

    var profile = codegenProfilesProperties.requireProfile(ProfileType.SPRINGBOOT_MAVEN_JAVA);
    var orderedArtifactKeys = profile.orderedArtifactKeys();

    List<ArtifactPort> ordered =
        orderedArtifactKeys.stream()
            .map(
                key -> {
                  ArtifactPort port = springBootMavenJavaArtifactRegistry.get(key);
                  if (port == null) {
                    throw new ProfileConfigurationException(
                        "bootstrap.artifact.not.found",
                        key.key(),
                        ProfileType.SPRINGBOOT_MAVEN_JAVA.key());
                  }
                  if (!port.artifactKey().equals(key)) {
                    throw new ArtifactKeyMismatchException(key, port.artifactKey());
                  }
                  return port;
                })
            .toList();

    return new SpringBootMavenJavaArtifactsAdapter(ordered);
  }
}
