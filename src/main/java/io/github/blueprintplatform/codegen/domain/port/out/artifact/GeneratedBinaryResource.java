package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.requireBinaryContent;
import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.requireRelativePath;

import java.nio.file.Path;

public record GeneratedBinaryResource(Path relativePath, BinaryContent content)
    implements GeneratedResource {

  public GeneratedBinaryResource {
    requireRelativePath(relativePath);
    requireBinaryContent(content);
  }

  public byte[] bytes() {
    return content.bytes();
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return "GeneratedBinaryResource[" + relativePath + ", " + content + "]";
  }
}
