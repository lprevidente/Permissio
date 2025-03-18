package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.util.TraversableUtils.get;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record ByMember(String property) implements Restriction<Requester> {

  public ByMember {
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
