package com.lprevidente.permissio.restriction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
  @Type(value = ById.class, name = "byId"),
  @Type(value = ByHandler.class, name = "byHandler"),
  @Type(value = ByCreator.class, name = "byCreator"),
  @Type(value = ByMember.class, name = "byMember"),
  @Type(value = ByRelatedEntity.class, name = "byRelatedEntity"),
  @Type(value = And.class, name = "and"),
  @Type(value = Or.class, name = "or"),
  @Type(value = Conjunction.class, name = "*"),
  @Type(value = Disjunction.class, name = "-")
})
@SuppressWarnings("rawtypes")
public interface Restriction<R extends Requester> {

  default Path get(Path<?> path, String property) {
    final var fields = property.split("\\.");
    return Arrays.stream(fields).reduce(path, Path::get, (a, b) -> b);
  }

  /**
   * Transform the restriction to Predicate
   *
   * @param requester
   * @param path table from which the predicate is generated
   * @param cb {@link CriteriaBuilder} mandatory to create query
   */
  Predicate toPredicate(
      R requester, //
      Path<?> path,
      CriteriaBuilder cb);
}
