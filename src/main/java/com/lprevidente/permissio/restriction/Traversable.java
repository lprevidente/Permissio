package com.lprevidente.permissio.restriction;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Traversable {

  // Cannot be created
  private Traversable() {}

  protected static Path get(Path<?> path, Map<String, Join> joinMap, String property) {
    final var fields = property.split("\\.");
    if (fields.length == 1) return path.get(fields[0]);

    Path from = path;
    for (int i = 0; i < fields.length - 1; i++) {
      var field = fields[i];
      final var key = getKeyJoin(from, field);

      if (!joinMap.containsKey(key)) joinMap.put(key, ((From) from).join(field, JoinType.LEFT));
      from = joinMap.get(key);
    }

    return from.get(fields[fields.length - 1]);
  }

  protected static From join(Path<?> path, Map<String, Join<?, ?>> joinMap, String field) {
    final var key = getKeyJoin(path, field);

    if (joinMap.containsKey(key)) return joinMap.get(key);
    final var join = ((From) path).join(field, JoinType.LEFT);

    joinMap.put(key, join);
    return join;
  }

  private static String getKeyJoin(Path<?> path, String field) {
    return path.getModel().toString().concat(".").concat(field);
  }
}
