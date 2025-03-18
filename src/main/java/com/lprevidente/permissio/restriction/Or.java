package com.lprevidente.permissio.restriction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public record Or(Restriction... restrictions) implements Restriction<Requester> {

  public Or {
    Assert.notNull(restrictions, "restrictions cannot be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> join) {
    return Arrays.stream(restrictions)
        .map(restriction -> restriction.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }
}
