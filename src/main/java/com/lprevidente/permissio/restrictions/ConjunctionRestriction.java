package com.lprevidente.permissio.restrictions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class ConjunctionRestriction implements Restriction<Object> {

  @Override
  public boolean isSatisfiedBy(Requester requester, Object baseEntity) {
    return true;
  }

  @Override
  public Predicate toPredicate(
      Requester requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
    return cb.conjunction();
  }
}