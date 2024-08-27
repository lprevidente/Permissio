package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = AccessByIdRestriction.class, name = "accessById"),
  @JsonSubTypes.Type(value = AccessByHandlersRestriction.class, name = "accessByHandlers"),
    @JsonSubTypes.Type(value = AccessByHandlerRestriction.class, name = "accessByHandler"),
  @JsonSubTypes.Type(value = AccessByCreatorRestriction.class, name = "accessByCreator"),
  @JsonSubTypes.Type(value = AccessByMemberRestriction.class, name = "accessByMember"),
  @JsonSubTypes.Type(
      value = AccessByRelatedEntityRestriction.class,
      name = "accessByRelatedEntity"),
  @JsonSubTypes.Type(value = AndRestriction.class, name = "and"),
  @JsonSubTypes.Type(value = OrRestriction.class, name = "or")
})
public interface Restriction<T> {

  boolean isSatisfiedBy(Requester requester, T obj);

  Predicate toPredicate(
      Requester requester,
      Path<? extends T> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> joinMap);
}
