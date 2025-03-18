package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.restriction.Traversable.get;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public record AccessByRelatedEntity(String property, Restriction restriction)
    implements Restriction<Requester> {

  public AccessByRelatedEntity {
    Assert.hasText(property, "property must not be empty");
    Assert.notNull(restriction, "restriction must not be null");
  }

  public Restriction getRestriction() {
    return restriction;
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> join) {
    final var lastPath = get(path, join, property);
    return restriction.toPredicate(requester, lastPath, cb, join);
  }
}
