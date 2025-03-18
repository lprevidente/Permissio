package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.restriction.Traversable.get;

import jakarta.persistence.criteria.*;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record AccessByCreator(String property) implements Restriction<Requester> {

  public AccessByCreator {
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> joinMap) {

    final var creatorPath = get(path, joinMap, property);
    return cb.equal(creatorPath, requester.getId());
  }
}
