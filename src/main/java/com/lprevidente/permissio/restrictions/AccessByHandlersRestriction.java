package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.HandlerEntity;
import com.lprevidente.permissio.entity.Handlers;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.util.Assert;

public class AccessByHandlersRestriction extends Traversable implements Restriction<Handlers> {
  private final String type;

  public AccessByHandlersRestriction() {
    this("*", "handlers:id");
  }

  public AccessByHandlersRestriction(String property) {
    this("*", property);
  }

  public AccessByHandlersRestriction(String type, String property) {
    super(property);
    Assert.isTrue(fields.size() <= 3, "Property must have at most 3 fields");
    Assert.hasText(type, "Type must not be empty");
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean isSatisfiedBy(Requester requester, Handlers handlers) {
    return handlers.getHandlers().stream()
        .filter(handler -> "*".equals(type) || handler.getType().equals(type))
        .map(HandlerEntity::getHandlerId)
        .anyMatch(id -> id == requester.getId());
  }

  @Override
  public Predicate toPredicate(
      Requester requester,
      Path<? extends Handlers> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {

    final var lastPath = getLastPath(path, join);

    if (lastPath.getJavaType() == Long.class) {
      if ("*".equals(type)) return cb.equal(lastPath, requester.getId());

      final var expType = getLastPath(path, join, typeArray());
      return cb.and(cb.equal(expType, type), cb.equal(lastPath, requester.getId()));
    }

    final var idPath = lastPath.get("id");
    if ("*".equals(type)) return cb.equal(idPath, requester.getId());

    final var expType = getLastPath(path, join, typeArray());
    return cb.and(cb.equal(expType, type), cb.equal(idPath, requester.getId()));
  }

  private List<String> typeArray() {
    final var copy = new ArrayList<>(fields);
    if (copy.size() == 3) copy.remove(2);

    if (copy.size() == 2) copy.set(1, "type");
    else copy.add("type");
    return copy;
  }
}
