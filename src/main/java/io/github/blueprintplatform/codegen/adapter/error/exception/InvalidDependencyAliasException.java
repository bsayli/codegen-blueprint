package io.github.blueprintplatform.codegen.adapter.error.exception;

@SuppressWarnings("java:S110")
public class InvalidDependencyAliasException extends AdapterException {

  private static final String KEY = "adapter.dependency.alias.unknown";

  public InvalidDependencyAliasException(String alias) {
    super(KEY, alias);
  }
}
