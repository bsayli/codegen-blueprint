package io.github.bsayli.codegen.initializr.bootstrap.wiring;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ArtifactKeyMismatchException;
import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.adapter.out.profile.ProfileType;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.SpringBootMavenJavaArtifactsAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.build.MavenPomBuildConfigurationAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.config.ApplicationYamlAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.docs.ProjectDocumentationAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.ignore.GitIgnoreAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.source.MainSourceEntrypointAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.test.TestSourceEntrypointAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.wrapper.MavenWrapperBuildToolFilesAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.bootstrap.error.exception.ProfileConfigurationException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootMavenJavaConfig {

  @Bean
  MavenPomBuildConfigurationAdapter springBootMavenJavaMavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.BUILD_CONFIG);
    return new MavenPomBuildConfigurationAdapter(renderer, props, pomDependencyMapper);
  }

  @Bean
  MavenWrapperBuildToolFilesAdapter springBootMavenJavaMavenWrapperBuildToolFilesAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.BUILD_TOOL_METADATA);
    return new MavenWrapperBuildToolFilesAdapter(renderer, props);
  }

  @Bean
  GitIgnoreAdapter springBootMavenJavaGitIgnoreAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.IGNORE_RULES);
    return new GitIgnoreAdapter(renderer, props);
  }

  @Bean
  ApplicationYamlAdapter springBootMavenJavaApplicationYamlAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.APP_CONFIG);
    return new ApplicationYamlAdapter(renderer, props);
  }

  @Bean
  MainSourceEntrypointAdapter springBootMavenJavaMainSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.MAIN_SOURCE_ENTRY_POINT);
    return new MainSourceEntrypointAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  TestSourceEntrypointAdapter springBootMavenJavaTestSourceEntrypointAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.TEST_SOURCE_ENTRY_POINT);
    return new TestSourceEntrypointAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  ProjectDocumentationAdapter springBootMavenJavaProjectDocumentationAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.PROJECT_DOCUMENTATION);
    return new ProjectDocumentationAdapter(renderer, props, pomDependencyMapper);
  }

  @Bean
  Map<ArtifactKey, ArtifactPort> springBootMavenJavaArtifactRegistry(
      MavenPomBuildConfigurationAdapter springBootMavenJavaMavenPomBuildConfigurationAdapter,
      MavenWrapperBuildToolFilesAdapter springBootMavenJavaMavenWrapperBuildToolFilesAdapter,
      GitIgnoreAdapter springBootMavenJavaGitIgnoreAdapter,
      ApplicationYamlAdapter springBootMavenJavaApplicationYamlAdapter,
      MainSourceEntrypointAdapter springBootMavenJavaMainSourceEntrypointAdapter,
      TestSourceEntrypointAdapter springBootMavenJavaTestSourceEntrypointAdapter,
      ProjectDocumentationAdapter springBootMavenJavaProjectDocumentationAdapter) {

    Map<ArtifactKey, ArtifactPort> registry = new EnumMap<>(ArtifactKey.class);
    registry.put(ArtifactKey.BUILD_CONFIG, springBootMavenJavaMavenPomBuildConfigurationAdapter);
    registry.put(
        ArtifactKey.BUILD_TOOL_METADATA, springBootMavenJavaMavenWrapperBuildToolFilesAdapter);
    registry.put(ArtifactKey.IGNORE_RULES, springBootMavenJavaGitIgnoreAdapter);
    registry.put(ArtifactKey.APP_CONFIG, springBootMavenJavaApplicationYamlAdapter);
    registry.put(
        ArtifactKey.MAIN_SOURCE_ENTRY_POINT, springBootMavenJavaMainSourceEntrypointAdapter);
    registry.put(
        ArtifactKey.TEST_SOURCE_ENTRY_POINT, springBootMavenJavaTestSourceEntrypointAdapter);
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
