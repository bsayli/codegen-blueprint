package io.github.bsayli.codegen.initializr.bootstrap.wiring;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ArtifactKeyMismatchException;
import io.github.bsayli.codegen.initializr.adapter.out.SpringBootMavenJavaArtifactsAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.build.MavenPomAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.config.ApplicationYamlAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.docs.ReadmeAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.source.SourceScaffolderAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.test.TestScaffolderAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.vcs.GitIgnoreAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.wrapper.MavenWrapperAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.adapter.profile.ProfileType;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifacts.ArtifactPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.bootstrap.config.CodegenProfilesProperties;
import io.github.bsayli.codegen.initializr.bootstrap.error.ProfileConfigurationException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootMavenJavaConfig {

  @Bean
  MavenPomAdapter springBootMavenJavaPomAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.POM);
    return new MavenPomAdapter(renderer, props, pomDependencyMapper);
  }

  @Bean
  MavenWrapperAdapter springBootMavenJavaWrapperAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.MAVEN_WRAPPER);
    return new MavenWrapperAdapter(renderer, props);
  }

  @Bean
  GitIgnoreAdapter springBootMavenJavaGitIgnoreAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.GITIGNORE);
    return new GitIgnoreAdapter(renderer, props);
  }

  @Bean
  ApplicationYamlAdapter springBootMavenJavaApplicationYamlAdapter(
      TemplateRenderer renderer, CodegenProfilesProperties profiles) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.APPLICATION_YAML);
    return new ApplicationYamlAdapter(renderer, props);
  }

  @Bean
  SourceScaffolderAdapter springBootMavenJavaSourceScaffolderAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.SOURCE_SCAFFOLDER);
    return new SourceScaffolderAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  TestScaffolderAdapter springBootMavenJavaTestScaffolderAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      StringCaseFormatter stringCaseFormatter) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.TEST_SCAFFOLDER);
    return new TestScaffolderAdapter(renderer, props, stringCaseFormatter);
  }

  @Bean
  ReadmeAdapter springBootMavenJavaReadmeAdapter(
      TemplateRenderer renderer,
      CodegenProfilesProperties profiles,
      PomDependencyMapper pomDependencyMapper) {
    ArtifactDefinition props =
        profiles.artifact(ProfileType.SPRINGBOOT_MAVEN_JAVA, ArtifactKey.README);
    return new ReadmeAdapter(renderer, props, pomDependencyMapper);
  }

  @Bean
  Map<ArtifactKey, ArtifactPort> springBootMavenJavaArtifactRegistry(
      MavenPomAdapter springBootMavenJavaPomAdapter,
      MavenWrapperAdapter springBootMavenJavaWrapperAdapter,
      GitIgnoreAdapter springBootMavenJavaGitIgnoreAdapter,
      ApplicationYamlAdapter springBootMavenJavaApplicationYamlAdapter,
      SourceScaffolderAdapter springBootMavenJavaSourceScaffolderAdapter,
      TestScaffolderAdapter springBootMavenTestScaffolderAdapter,
      ReadmeAdapter springBootMavenJavaReadmeAdapter) {

    Map<ArtifactKey, ArtifactPort> registry = new EnumMap<>(ArtifactKey.class);
    registry.put(ArtifactKey.POM, springBootMavenJavaPomAdapter);
    registry.put(ArtifactKey.MAVEN_WRAPPER, springBootMavenJavaWrapperAdapter);
    registry.put(ArtifactKey.GITIGNORE, springBootMavenJavaGitIgnoreAdapter);
    registry.put(ArtifactKey.APPLICATION_YAML, springBootMavenJavaApplicationYamlAdapter);
    registry.put(ArtifactKey.SOURCE_SCAFFOLDER, springBootMavenJavaSourceScaffolderAdapter);
    registry.put(ArtifactKey.TEST_SCAFFOLDER, springBootMavenTestScaffolderAdapter);
    registry.put(ArtifactKey.README, springBootMavenJavaReadmeAdapter);
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
