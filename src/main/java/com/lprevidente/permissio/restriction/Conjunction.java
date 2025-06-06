package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@SuppressWarnings({"rawtypes"})
public record Conjunction() implements Restriction<Requester> {

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    return cb.conjunction();
  }
}
