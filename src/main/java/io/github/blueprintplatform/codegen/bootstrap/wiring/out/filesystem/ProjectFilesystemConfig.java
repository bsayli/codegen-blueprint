package io.github.blueprintplatform.codegen.bootstrap.wiring.out.filesystem;

import io.github.blueprintplatform.codegen.adapter.out.filesystem.FileSystemProjectArchiverAdapter;
import io.github.blueprintplatform.codegen.adapter.out.filesystem.FileSystemProjectRootAdapter;
import io.github.blueprintplatform.codegen.adapter.out.filesystem.FileSystemProjectWriterAdapter;
import io.github.blueprintplatform.codegen.application.port.out.archive.ProjectArchiverPort;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootPort;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectWriterPort;
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
