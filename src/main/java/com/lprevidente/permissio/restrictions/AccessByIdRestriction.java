package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class AccessByIdRestriction<ID> implements Restriction<BaseEntity<ID>> {
  private final ID id;

  @JsonCreator
  public AccessByIdRestriction(@JsonProperty("id") ID id) {
    this.id = id;
  }

  public ID getId() {
    return id;
  }

  @Override
  public boolean isSatisfiedBy(Requester requester, BaseEntity<ID> baseEntity) {
    return baseEntity.getId() == id;
  }

  @Override
  public Predicate toPredicate(
      Requester requester,
      Path<? extends BaseEntity<ID>> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {

    return cb.equal(path.get("id"), id);
  }
}
