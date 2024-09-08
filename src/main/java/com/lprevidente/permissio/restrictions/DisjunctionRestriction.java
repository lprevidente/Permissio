package com.lprevidente.permissio.restrictions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class DisjunctionRestriction<RequesterId>
    implements Restriction<Object, Requester<RequesterId>> {

  @Override
  public boolean isSatisfiedBy(Requester<RequesterId> requester, Object baseEntity) {
    return false;
  }

  @Override
  public Predicate toPredicate(
      Requester<RequesterId> requester,
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {
    return cb.disjunction();
  }
}
