package io.github.blueprintplatform.codegen.adapter.in.cli.shared;

import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;
import java.util.function.Function;
import picocli.CommandLine.ITypeConverter;

public final class KeyedEnumConverter<E extends Enum<E> & KeyedEnum> implements ITypeConverter<E> {

  private final Function<String, E> delegate;

  public KeyedEnumConverter(Function<String, E> delegate) {
    this.delegate = delegate;
  }

  @Override
  public E convert(String value) {
    return delegate.apply(value);
  }
}
