package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.*;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public record ByRelatedEntity(String property, Restriction restriction)
    implements Restriction<Requester> {

  public ByRelatedEntity {
    Assert.hasText(property, "property must not be empty");
    Assert.notNull(restriction, "restriction must not be null");
  }

  public Restriction getRestriction() {
    return restriction;
  }

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    final var lastPath = get(path, property);
    return restriction.toPredicate(requester, lastPath, cb);
  }
}
