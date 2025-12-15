package io.github.blueprintplatform.codegen.adapter.error.exception;

public final class ArchitectureGovernanceTemplatesScanException extends AdapterException {

  private static final String KEY = "adapter.architecture-governance.templates.scan.failed";

  public ArchitectureGovernanceTemplatesScanException(String templateRoot, Throwable cause) {
    super(KEY, cause, templateRoot);
  }
}
