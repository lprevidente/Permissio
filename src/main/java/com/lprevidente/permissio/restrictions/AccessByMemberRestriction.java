package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Group;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

public class AccessByMemberRestriction extends Traversable implements Restriction<Group> {

  public AccessByMemberRestriction() {
    super("members");
  }

  public AccessByMemberRestriction(@JsonProperty("property") String property) {
    super(property);
    Assert.isTrue(fields.size() <= 3, "Property must have at most 3 fields");
  }

  @Override
  public boolean isSatisfiedBy(Requester requester, Group obj) {
    return obj.getMembers().stream().map(BaseEntity::getId).anyMatch(id -> id == requester.getId());
  }

  @Override
  public Predicate toPredicate(
      Requester requester,
      Path<? extends Group> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {
    final var lastPath = getLastPath(path, join);
    if (lastPath.getJavaType() == Long.class) return cb.equal(lastPath, requester.getId());

    return cb.equal(lastPath.get("id"), requester.getId());
  }
}
