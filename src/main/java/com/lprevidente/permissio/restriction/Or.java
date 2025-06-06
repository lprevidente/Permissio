package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public record Or(Restriction... restrictions) implements Restriction<Requester> {

  public Or {
    Assert.notNull(restrictions, "restrictions cannot be null");
  }

  @Override
  public Predicate toPredicate(Requester requester, Path<?> path, CriteriaBuilder cb) {
    // Create multiple subquery and to each subquery apply the predicate of the restriction
    return Arrays.stream(restrictions)
        .map(restriction -> applyPredicate(requester, restriction, path, cb))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  private Predicate applyPredicate(
      Requester requester, //
      Restriction restriction,
      Path<?> path,
      CriteriaBuilder cb) {
    final var subquery = cb.createQuery(Long.class).subquery(Long.class);
    final var subqueryRoot = subquery.from(path.getJavaType());
    subquery.select(cb.literal(1L));
    subquery.where(
        cb.and(
            restriction.toPredicate(requester, subqueryRoot, cb),
            cb.equal(path.get("id"), subqueryRoot.get("id"))));
    return cb.exists(subquery);
  }
}
