package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record ById(String property, Object id) implements Restriction<Requester> {

  public ById {
    Assert.notNull(id, "id cannot be null");
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    return cb.equal(path.get(property), id);
  }
}
