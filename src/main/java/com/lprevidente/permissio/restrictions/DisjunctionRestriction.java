package com.lprevidente.permissio.restrictions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class DisjunctionRestriction implements Restriction<Object> {

  @Override
  public boolean isSatisfiedBy(Requester requester, Object baseEntity) {
    return false;
  }

  @Override
  public Predicate toPredicate(
      Requester requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
    return cb.disjunction();
  }
}
