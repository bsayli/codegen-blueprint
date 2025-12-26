package io.github.blueprintplatform.codegen.adapter.out.build.shared;

public record BuildDependency(
    String groupId, String artifactId, String version, String scope, String versionPropertyKey) {

  public static BuildDependency of(String groupId, String artifactId) {
    return new BuildDependency(groupId, artifactId, null, null, null);
  }

  public static BuildDependency of(
      String groupId, String artifactId, String version, String scope) {
    return new BuildDependency(groupId, artifactId, version, scope, null);
  }

  public static BuildDependency ofWithVersionProperty(
      String groupId, String artifactId, String versionPropertyKey, String scope) {
    return new BuildDependency(groupId, artifactId, null, scope, versionPropertyKey);
  }
}
