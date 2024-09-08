package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.entity.Creatable;
import jakarta.persistence.criteria.*;
import java.util.Map;
import org.springframework.util.Assert;

public class AccessByCreatorRestriction<RequesterID> extends Traversable
    implements Restriction<Creatable<RequesterID>, Requester<RequesterID>> {

  public AccessByCreatorRestriction() {
    super("creatorId");
  }

  @JsonCreator
  public AccessByCreatorRestriction(@JsonProperty("property") String property) {
    super(property);
    Assert.isTrue(fields.size() <= 2, "Property must have at most 2 fields");
  }

  @Override
  public boolean isSatisfiedBy(Requester<RequesterID> requester, Creatable<RequesterID> obj) {
    return obj.getCreatorId().equals(requester.getId());
  }

  @Override
  public Predicate toPredicate(
      Requester<RequesterID> requester,
      Path<? extends Creatable<RequesterID>> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> joinMap) {

    final var fieldPath = path.get(fields.getFirst());
    final var returnType = fieldPath.getJavaType();

    if (fields.size() == 1 && returnType == Long.class)
      return cb.equal(fieldPath, requester.getId());

    final var lastField = fields.size() > 1 ? fields.get(1) : "id";
    final var lastPath = fieldPath.get(lastField);

    return cb.equal(lastPath, requester.getId());
  }
}
