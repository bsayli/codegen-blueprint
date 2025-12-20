package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.requireBinaryContent;

import java.util.Arrays;

@SuppressWarnings("java:S6206")
public final class BinaryContent {

  private final byte[] bytes;

  public BinaryContent(byte[] bytes) {
    requireBinaryContent(bytes);
    this.bytes = Arrays.copyOf(bytes, bytes.length);
  }

  public byte[] bytes() {
    return Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public boolean equals(Object o) {
    return (this == o)
        || (o instanceof BinaryContent other && Arrays.equals(this.bytes, other.bytes));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(bytes);
  }

  @Override
  public String toString() {
    return "BinaryContent[size=" + bytes.length + "]";
  }
}
