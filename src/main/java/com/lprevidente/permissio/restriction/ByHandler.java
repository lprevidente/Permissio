package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes"})
public record ByHandler(Type type, Id id) implements Restriction<Requester> {

  public ByHandler {
    Assert.notNull(type, "Type must not be null");
    Assert.notNull(id, "Field must not be null");
  }

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    final var idPath = get(path, id.property);
    if ("*".equals(type.type)) return cb.equal(idPath, requester.getId());

    final var typePath = get(path, type.property);
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
