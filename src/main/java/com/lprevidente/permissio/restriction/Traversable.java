package com.lprevidente.permissio.restriction;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.util.Assert;

public abstract class Traversable {
  protected final String property;
  protected final List<String> fields;

  protected Traversable(String property) {
    Assert.hasText(property, "Properties must not be empty");
    this.property = property;
    this.fields = new ArrayList<>(Arrays.asList(property.split(":")));
  }

  public String getProperty() {
    return property;
  }

  protected From<?, ?> joinLastPath(Path<?> path, Map<String, Join<?, ?>> joinMap) {
    final var penultimatePath = getPenultimatePath(path, joinMap, fields);
    return penultimatePath.join(fields.getLast(), JoinType.LEFT);
  }

  protected Path<?> getLastPath(
      Path<?> path, Map<String, Join<?, ?>> joinMap, List<String> fields) {
    final var penultimatePath = getPenultimatePath(path, joinMap, fields);
    return penultimatePath.get(fields.getLast());
  }

  protected Path getLastPath(Path<?> path, Map<String, Join<?, ?>> joinMap) {
    return getLastPath(path, joinMap, fields);
  }

  protected From<?, ?> getPenultimatePath(
      Path<?> path, Map<String, Join<?, ?>> joinMap, List<String> fields) {

    // Initialize the current path and join key
    var currentPath = path;

    // Iterate over fields except the last one to handle joins
    for (int i = 0; i < fields.size() - 1; i++) {
      final var field = fields.get(i);

      // Check if join already exists in the map
      if (joinMap.containsKey(field)) {
        currentPath = joinMap.get(field);
      } else {
        // If not, create a new join and add it to the map
        final var from = (From<?, ?>) currentPath;
        final var join = from.join(field, JoinType.LEFT);
        joinMap.put(field, join);
        currentPath = join;
      }
    }

    return (From<?, ?>) currentPath;
  }
}