package io.github.bsayli.codegen.initializr.projectgeneration.ports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface ProjectArchiver {

  Path archiveProject(File projectDestination, String projectName) throws IOException;
}
