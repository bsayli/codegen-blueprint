package io.github.blueprintplatform.codegen.adapter.out.build.maven.shared;

public record PomDependency(
    String groupId, String artifactId, String version, String scope, String versionPropertyKey) {

  public static PomDependency of(String groupId, String artifactId) {
    return new PomDependency(groupId, artifactId, null, null, null);
  }

  public static PomDependency of(String groupId, String artifactId, String version, String scope) {
    return new PomDependency(groupId, artifactId, version, scope, null);
  }

  public static PomDependency ofWithVersionProperty(
      String groupId, String artifactId, String versionPropertyKey, String scope) {
    return new PomDependency(groupId, artifactId, null, scope, versionPropertyKey);
  }
}
