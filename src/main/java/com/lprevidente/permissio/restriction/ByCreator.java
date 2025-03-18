package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.util.TraversableUtils.get;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.*;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public record ByCreator(String property) implements Restriction<Requester> {

  public ByCreator {
    Assert.hasText(property, "property must not be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> joinMap) {

    final var creatorPath = get(path, joinMap, property);
    return cb.equal(creatorPath, requester.getId());
  }
}
