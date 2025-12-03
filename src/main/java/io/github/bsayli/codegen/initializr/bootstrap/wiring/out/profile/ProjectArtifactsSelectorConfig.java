package io.github.bsayli.codegen.initializr.bootstrap.wiring.out.profile;

import io.github.bsayli.codegen.initializr.adapter.out.profile.ProfileBasedArtifactsSelector;
import io.github.bsayli.codegen.initializr.adapter.out.profile.ProfileType;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsSelector;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectArtifactsSelectorConfig {

  @Bean
  public Map<ProfileType, ProjectArtifactsPort> projectArtifactsPortRegistry(
      ProjectArtifactsPort springBootMavenJavaArtifactsAdapter) {

    Map<ProfileType, ProjectArtifactsPort> registry = new EnumMap<>(ProfileType.class);
    registry.put(ProfileType.SPRINGBOOT_MAVEN_JAVA, springBootMavenJavaArtifactsAdapter);
    return Collections.unmodifiableMap(registry);
  }

  @Bean
  public ProjectArtifactsSelector projectArtifactsSelector(
      Map<ProfileType, ProjectArtifactsPort> projectArtifactsPortRegistry) {

    return new ProfileBasedArtifactsSelector(projectArtifactsPortRegistry);
  }
}
