package io.github.blueprintplatform.codegen.domain.policy.file;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class GeneratedFilePolicy {
  private GeneratedFilePolicy() {}

  public static void requireRelativePath(Path path) {
    if (path == null) throw new DomainViolationException(() -> "file.path.not.blank");
    if (path.isAbsolute())
      throw new DomainViolationException(() -> "file.path.absolute.not.allowed");
    if (path.getNameCount() == 0) throw new DomainViolationException(() -> "file.path.not.blank");
    for (Path part : path) {
      String s = part.toString();
      if (s.isEmpty() || ".".equals(s) || "..".equals(s)) {
        throw new DomainViolationException(() -> "file.path.traversal.not.allowed");
      }
    }
  }

  public static void requireTextContent(CharSequence content, Charset charset) {
    if (content == null) throw new DomainViolationException(() -> "file.content.not.blank");
    if (charset == null) throw new DomainViolationException(() -> "file.charset.not.blank");
  }

  public static void requireBinaryContent(byte[] bytes) {
    if (bytes == null) throw new DomainViolationException(() -> "file.content.not.blank");
  }
}
