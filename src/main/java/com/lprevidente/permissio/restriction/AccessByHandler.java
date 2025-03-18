package com.lprevidente.permissio.restriction;

import static com.lprevidente.permissio.restriction.Traversable.get;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes"})
public record AccessByHandler(Type type, Id id) implements Restriction<Requester> {

  public AccessByHandler {
    Assert.notNull(type, "Type must not be null");
    Assert.notNull(id, "Field must not be null");
  }

  @Override
  public Predicate toPredicate(
      Requester requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> join) {

    final var idPath = get(path, join, id.property);
    if ("*".equals(type.type)) return cb.equal(idPath, requester.getId());

    final var typePath = get(path, join, type.property);
    return cb.and(cb.equal(typePath, type.type), cb.equal(idPath, requester.getId()));
  }

  public record Type(String type, String property) {

    public Type {
      Assert.notNull(type, "Type must not be null");
      Assert.notNull(property, "Property must not be null");
    }
  }

  public record Id(String field, String property) {

    public Id {
      Assert.notNull(field, "Field must not be null");
      Assert.notNull(property, "property must not be null");
    }
  }
}
