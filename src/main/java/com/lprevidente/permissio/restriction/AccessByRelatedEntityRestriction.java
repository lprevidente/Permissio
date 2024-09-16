package com.lprevidente.permissio.restriction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.springframework.util.Assert;

public class AccessByRelatedEntityRestriction extends Traversable
    implements Restriction<Object, Requester<?>> {

  private final Restriction restriction;

  @JsonCreator
  public AccessByRelatedEntityRestriction(
      @JsonProperty("property") String property,
      @JsonProperty("restriction") Restriction restriction) {
    super(property);
    Assert.isTrue(fields.size() <= 2, "Property must have at most 2 fields");
    Assert.notNull(restriction, "Properties must not be empty");
    this.restriction = restriction;
  }

  public Restriction getRestriction() {
    return restriction;
  }

  @Override
  public boolean isSatisfiedBy(Requester<?> requester, Object baseEntity) {
    final var res = getRelatedEntity(baseEntity, fields.get(0));

    if (res instanceof Iterable<?> iterable) {

      if (fields.size() == 2) {
        return StreamSupport.stream(iterable.spliterator(), false)
            .map(entity -> getRelatedEntity(entity, fields.get(1)))
            .anyMatch(entity -> restriction.isSatisfiedBy(requester, entity));
      }

      return StreamSupport.stream(iterable.spliterator(), false)
          .map(BaseEntity.class::cast)
          .anyMatch(entity -> restriction.isSatisfiedBy(requester, entity));
    }

    return restriction.isSatisfiedBy(requester, res);
  }

  private Object getRelatedEntity(Object baseEntity, String property) {
    try {
      final var methodName = "get" + capitalizeFirstLetter(property);
      final var method = baseEntity.getClass().getMethod(methodName);
      return method.invoke(baseEntity);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Predicate toPredicate(
      Requester<?> requester, Path<?> path, CriteriaBuilder cb, Map<String, Join<?, ?>> join) {
    final var lastPath = joinLastPath(path, join);
    return restriction.toPredicate(requester, lastPath, cb, join);
  }

  private String capitalizeFirstLetter(String input) {
    // Return the string as it is if it's null or empty
    if (input == null || input.isEmpty()) return input;
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }
}
