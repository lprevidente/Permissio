package com.lprevidente.permissio.restriction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Map;

public class OrRestriction implements Restriction<Object, Requester<?>> {
  private final Restriction[] restrictions;

  @JsonCreator
  public OrRestriction(@JsonProperty("restrictions") Restriction... restrictions) {
    this.restrictions = restrictions;
  }

  public Restriction<?, Requester<?>>[] getRestrictions() {
    return restrictions;
  }

  @Override
  public boolean isSatisfiedBy(Requester<?> requester, Object baseEntity) {
    return Arrays.stream(restrictions)
        .anyMatch(restriction -> restriction.isSatisfiedBy(requester, baseEntity));
  }

  @Override
  public Predicate toPredicate(
      Requester<?> requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
    return Arrays.stream(restrictions)
        .map(restriction -> restriction.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }
}
