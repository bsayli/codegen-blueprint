package io.github.blueprintplatform.codegen.domain.policy.rule.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CompositeRule<T> implements Rule<T> {

  private final List<Rule<? super T>> rules;

  private CompositeRule(List<Rule<? super T>> rules) {
    this.rules = List.copyOf(rules);
  }

  @SafeVarargs
  public static <T> CompositeRule<T> of(Rule<? super T>... rules) {
    List<Rule<? super T>> list = Arrays.asList(rules);
    return new CompositeRule<>(list);
  }

  public static <T> CompositeRule<T> of(List<? extends Rule<? super T>> rules) {
    return new CompositeRule<>(new ArrayList<>(rules));
  }

  @Override
  public void check(T value) {
    for (Rule<? super T> r : rules) {
      r.check(value);
    }
  }
}
