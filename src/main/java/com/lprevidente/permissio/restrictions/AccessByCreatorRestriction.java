package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.Creatable;
import jakarta.persistence.criteria.*;
import java.util.Map;
import org.springframework.util.Assert;

public class AccessByCreatorRestriction extends Traversable implements Restriction<Creatable> {

  public AccessByCreatorRestriction() {
    super("creatorId");
  }

  public AccessByCreatorRestriction(String property) {
    super(property);
    Assert.isTrue(fields.size() <= 2, "Property must have at most 2 fields");
  }

  @Override
  public boolean isSatisfiedBy(Requester requester, Creatable obj) {
    return obj.getCreatorId() == requester.getId();
  }

  @Override
  public Predicate toPredicate(
      Requester requester,
      Path<? extends Creatable> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> joinMap) {

    final var fieldPath = path.get(fields.get(0));
    final var returnType = fieldPath.getJavaType();

    if (fields.size() == 1 && returnType == Long.class)
      return cb.equal(fieldPath, requester.getId());

    final var lastField = fields.size() > 1 ? fields.get(1) : "id";
    final var lastPath = fieldPath.get(lastField);

    return cb.equal(lastPath, requester.getId());
  }
}
