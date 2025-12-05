package io.github.blueprintplatform.codegen.adapter.out.build.maven.shared;

public record PomDependency(String groupId, String artifactId, String version, String scope) {
  public static PomDependency of(String groupId, String artifactId) {
    return new PomDependency(groupId, artifactId, null, null);
  }

  public static PomDependency of(String groupId, String artifactId, String version, String scope) {
    return new PomDependency(groupId, artifactId, version, scope);
  }
}
