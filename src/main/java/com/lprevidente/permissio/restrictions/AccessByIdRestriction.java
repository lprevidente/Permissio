package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class AccessByIdRestriction implements Restriction<BaseEntity> {
  private final long id;

  @JsonCreator
  public AccessByIdRestriction(@JsonProperty("id") long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  public boolean isSatisfiedBy(Requester requester, BaseEntity baseEntity) {
    return baseEntity.getId() == id;
  }

  @Override
  public Predicate toPredicate(
      Requester requester,
      Path<? extends BaseEntity> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {

    return cb.equal(path.get("id"), id);
  }
}
