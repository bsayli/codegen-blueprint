package io.github.bsayli.codegen.initializr.projectgeneration.configuration;

import io.github.bsayli.codegen.initializr.projectgeneration.model.maven.MavenPlugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootMavenJavaProjectConfiguration {

  @Bean
  List<MavenPlugin> springBootMavenJavaPlugins() {
    List<MavenPlugin> springBootMavenPlugins = new ArrayList<>();

    MavenPlugin springBootMavenPlugin =
        new MavenPlugin.MavenPluginBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-maven-plugin")
            .build();

    MavenPlugin mavenCompilerPlugin =
        new MavenPlugin.MavenPluginBuilder()
            .groupId("org.apache.maven.plugins")
            .artifactId("maven-compiler-plugin")
            .addConfigurationElement("generatedSourcesDirectory", "src/gen/java")
            .addConfigurationElement("compileSourceRoots", List.of("src/main/java", "src/gen/java"))
            .build();

    springBootMavenPlugins.add(springBootMavenPlugin);
    springBootMavenPlugins.add(mavenCompilerPlugin);

    return Collections.unmodifiableList(springBootMavenPlugins);
  }
}
