package io.github.bsayli.codegen.initializr.projectgeneration.adapters.maven;

import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.MavenJavaSourceFolderProperties;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectLayoutGenerator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("mavenJavaProjectLayoutGenerator")
public class MavenJavaProjectLayoutGenerator implements ProjectLayoutGenerator {

  private final MavenJavaSourceFolderProperties sourceFolder;

  public MavenJavaProjectLayoutGenerator(MavenJavaSourceFolderProperties sourceFolder) {
    this.sourceFolder = sourceFolder;
  }

  @Override
  public void generateProjectLayout(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    generateSourceFolders(projectDestination);
    generatePackages(projectDestination, projectMetadata.getPackageName());
  }

  private void generateSourceFolders(File projectDestination) {
    sourceFolder
        .getSourceFolders()
        .forEach(
            s -> {
              File sourceDir = new File(projectDestination, s);
              if (!sourceDir.exists()) {
                sourceDir.mkdirs();
              }
            });
  }

  private void generatePackages(File projectDestination, String packageName) {
    String packageNamePath = packageName.replace(".", "/");
    packageNamePath = sanitizePackageName(packageNamePath);

    File sourceFolderMainJavaFile = new File(projectDestination, sourceFolder.srcMainJava());
    File packageJavaFile = new File(sourceFolderMainJavaFile, packageNamePath);

    File sourceFolderTestJavaFile = new File(projectDestination, sourceFolder.srcTestJava());
    File packageTestFile = new File(sourceFolderTestJavaFile, packageNamePath);

    String packageGenPath = packageNamePath + "/codegen";
    File sourceFolderGenJavaFile = new File(projectDestination, sourceFolder.srcGenJava());
    File packageGenFile = new File(sourceFolderGenJavaFile, packageGenPath);

    List<File> projectPackages = List.of(packageJavaFile, packageTestFile, packageGenFile);

    projectPackages.forEach(
        p -> {
          if (!p.exists()) {
            p.mkdirs();
          }
        });
  }

  private String sanitizePackageName(String packageName) {
    return packageName.replaceAll("[\\:*?<>|\\\\\\^]", "").replaceAll("-+", "_");
  }
}
