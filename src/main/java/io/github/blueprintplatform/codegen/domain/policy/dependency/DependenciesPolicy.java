package io.github.blueprintplatform.codegen.domain.policy.dependency;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import java.util.*;

public final class DependenciesPolicy {

  private static final ErrorCode LIST_REQUIRED = () -> "dependency.list.not.blank";
  private static final ErrorCode ITEM_REQUIRED = () -> "dependency.item.not.blank";
  private static final ErrorCode DUPLICATE_COORDS = () -> "dependency.duplicate.coordinates";

  private DependenciesPolicy() {}

  public static List<Dependency> enforce(List<Dependency> raw) {
    if (raw == null) {
      throw new DomainViolationException(LIST_REQUIRED);
    }
    if (raw.isEmpty()) {
      return List.of();
    }

    Map<String, Dependency> byCoords = getDependencyMap(raw);

    List<Dependency> list = new ArrayList<>(byCoords.values());
    list.sort(
        Comparator.comparing(
            dep ->
                dep.coordinates().groupId().value()
                    + ":"
                    + dep.coordinates().artifactId().value()));
    return List.copyOf(list);
  }

  private static Map<String, Dependency> getDependencyMap(List<Dependency> raw) {
    Map<String, Dependency> byCoords = new LinkedHashMap<>();
    for (Dependency d : raw) {
      if (d == null) {
        throw new DomainViolationException(ITEM_REQUIRED);
      }
      var coords = d.coordinates();
      var key = coords.groupId().value() + ":" + coords.artifactId().value();
      if (byCoords.containsKey(key)) {
        throw new DomainViolationException(DUPLICATE_COORDS, key);
      }
      byCoords.put(key, d);
    }
    return byCoords;
  }
}
