package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Map;

public class AndRestriction implements Restriction<Object, Requester<?>> {

  private final Restriction[] restrictions;

  @JsonCreator
  public AndRestriction(@JsonProperty("restrictions") Restriction... restrictions) {
    this.restrictions = restrictions;
  }

  public Restriction[] getRestrictions() {
    return restrictions;
  }

  @Override
  public boolean isSatisfiedBy(Requester<?> requester, Object baseEntity) {
    return Arrays.stream(restrictions)
        .allMatch(restriction -> restriction.isSatisfiedBy(requester, baseEntity));
  }

  @Override
  public Predicate toPredicate(
      Requester<?> requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
    return Arrays.stream(restrictions)
        .map(restriction -> restriction.toPredicate(requester, path, cb, join))
        .reduce(cb::and)
        .orElse(cb.conjunction());
  }
}
