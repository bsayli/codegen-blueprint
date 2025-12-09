package io.github.blueprintplatform.codegen.adapter.error.exception;

@SuppressWarnings("java:S110")
public final class SampleCodeTemplatesScanException extends AdapterException {

  private static final String KEY = "adapter.sample-code.templates.scan.failed";

  public SampleCodeTemplatesScanException(String templateRoot, Throwable cause) {
    super(KEY, cause, templateRoot);
  }
}
