package com.lprevidente.permissio.restriction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record AccessById(String property, Object id) implements Restriction<Requester> {

  public AccessById {
    Assert.notNull(id, "id cannot be null");
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> joinMap) {

    return cb.equal(path.get(property), id);
  }
}
