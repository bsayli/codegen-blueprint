package io.github.blueprintplatform.codegen.domain.policy.file;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.BinaryContent;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class GeneratedFilePolicy {

  private static final ErrorCode FILE_PATH_NOT_BLANK = () -> "file.path.not.blank";
  private static final ErrorCode FILE_PATH_ABSOLUTE_NOT_ALLOWED =
      () -> "file.path.absolute.not.allowed";
  private static final ErrorCode FILE_PATH_TRAVERSAL_NOT_ALLOWED =
      () -> "file.path.traversal.not.allowed";

  private static final ErrorCode FILE_CONTENT_NOT_BLANK = () -> "file.content.not.blank";
  private static final ErrorCode FILE_CHARSET_NOT_BLANK = () -> "file.charset.not.blank";

  private GeneratedFilePolicy() {}

  public static void requireRelativePath(Path path) {
    if (path == null) {
      throw new DomainViolationException(FILE_PATH_NOT_BLANK);
    }
    if (path.isAbsolute()) {
      throw new DomainViolationException(FILE_PATH_ABSOLUTE_NOT_ALLOWED);
    }
    if (path.getNameCount() == 0) {
      throw new DomainViolationException(FILE_PATH_NOT_BLANK);
    }
    for (Path part : path) {
      String s = part.toString();
      if (s.isEmpty() || ".".equals(s) || "..".equals(s)) {
        throw new DomainViolationException(FILE_PATH_TRAVERSAL_NOT_ALLOWED);
      }
    }
  }

  public static void requireTextContent(CharSequence content, Charset charset) {
    if (content == null) {
      throw new DomainViolationException(FILE_CONTENT_NOT_BLANK);
    }
    if (charset == null) {
      throw new DomainViolationException(FILE_CHARSET_NOT_BLANK);
    }
  }

  public static void requireBinaryContent(byte[] bytes) {
    if (bytes == null) {
      throw new DomainViolationException(FILE_CONTENT_NOT_BLANK);
    }
  }

  public static void requireBinaryContent(BinaryContent content) {
    if (content == null) {
      throw new DomainViolationException(FILE_CONTENT_NOT_BLANK);
    }
  }
}
