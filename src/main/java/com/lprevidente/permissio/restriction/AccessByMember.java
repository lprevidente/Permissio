package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.restriction.Traversable.get;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record AccessByMember(String property) implements Restriction<Requester> {

  public AccessByMember {
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> join) {
    final var memberIdPath = get(path, join, property);
    return cb.equal(memberIdPath, requester.getId());
  }
}
