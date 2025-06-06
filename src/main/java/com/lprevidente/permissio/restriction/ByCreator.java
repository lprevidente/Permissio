package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.*;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record ByCreator(String property) implements Restriction<Requester> {

  public ByCreator {
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    final var creatorPath = get(path, property);
    return cb.equal(creatorPath, requester.getId());
  }
}
