package com.lprevidente.permissio.restriction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
  @Type(value = AccessById.class, name = "byId"),
  @Type(value = AccessByHandler.class, name = "byHandler"),
  @Type(value = AccessByCreator.class, name = "byCreator"),
  @Type(value = AccessByMember.class, name = "byMember"),
  @Type(value = AccessByRelatedEntity.class, name = "byRelatedEntity"),
  @Type(value = And.class, name = "and"),
  @Type(value = Or.class, name = "or"),
  @Type(value = Conjunction.class, name = "*"),
  @Type(value = Disjunction.class, name = "-")
})
@SuppressWarnings("rawtypes")
public interface Restriction<R extends Requester> {

  /**
   * Transform the restriction to Predicate
   *
   * @param requester
   * @param path table from which the predicate is generated
   * @param cb {@link CriteriaBuilder} mandatory to create query
   * @param joinMap to avoid multiple joins with the same table
   */
  Predicate toPredicate(
      R requester, //
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join> joinMap);
}
