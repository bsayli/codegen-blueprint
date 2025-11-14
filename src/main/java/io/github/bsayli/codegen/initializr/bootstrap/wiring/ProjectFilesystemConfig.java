package io.github.bsayli.codegen.initializr.bootstrap.wiring;

import io.github.bsayli.codegen.initializr.adapter.out.filesystem.FileSystemProjectArchiverAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.filesystem.FileSystemProjectRootAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.filesystem.FileSystemProjectWriterAdapter;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectFilesystemConfig {

  @Bean
  public ProjectRootPort fileSystemProjectRootAdapter() {
    return new FileSystemProjectRootAdapter();
  }

  @Bean
  public ProjectWriterPort fileSystemProjectWriterAdapter() {
    return new FileSystemProjectWriterAdapter();
  }

  @Bean
  public ProjectArchiverPort fileSystemProjectArchiverAdapter() {
    return new FileSystemProjectArchiverAdapter();
  }
}
