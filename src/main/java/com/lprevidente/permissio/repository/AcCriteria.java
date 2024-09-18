package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.Relatable;
import com.lprevidente.permissio.restriction.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.Assert;

public class AcCriteria {
  private final Requester<?> requester;
  private final List<String> permissions;

  private AcCriteria(Requester<?> requester, List<String> permissions) {
    Assert.notNull(requester, "The requester cannot be null");
    this.permissions = permissions;
    this.requester = requester;
  }

  public static AcCriteriaBuilder builder() {
    return new AcCriteriaBuilder();
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public Requester<?> getRequester() {
    return requester;
  }

  public Predicate toPredicate(Path<?> path, CriteriaBuilder cb) {
    final var join = new HashMap<String, Join<?, ?>>();
    return permissions.stream()
        .map(p -> requester.getPermissions().getOrDefault(p, new DisjunctionRestriction()))
        .map(r -> r.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  public Predicate getPredicateRelated(Path<?> path, CriteriaBuilder cb) {
    try {
      final var join = new HashMap<String, Join<?, ?>>();

      final var obj = path.getJavaType().getConstructor().newInstance();
      if (!(obj instanceof Relatable)) return cb.disjunction();

      final var getKeyJoin = path.getJavaType().getDeclaredMethod("getKeyJoin", String.class);
      final var predicates = new ArrayList<Predicate>();

      for (var p : permissions) {
        final var key = getKeyJoin.invoke(obj, p);
        if (key == null) continue;

        final var restriction =
            requester.getPermissions().getOrDefault(p, new ConjunctionRestriction());
        AccessByRelatedEntityRestriction related = null;
        if (restriction instanceof OrRestriction or)
          related =
              Arrays.stream(or.getRestrictions())
                  .filter(AccessByRelatedEntityRestriction.class::isInstance)
                  .map(AccessByRelatedEntityRestriction.class::cast)
                  .filter(r -> r.getProperty().equals(key))
                  .findFirst()
                  .orElse(null);
        else if (restriction instanceof AccessByRelatedEntityRestriction r
            && r.getProperty().equals(key)) related = r;

        if (related != null)
          predicates.add(related.getRestriction().toPredicate(requester, path, cb, join));
      }

      if (predicates.isEmpty()) return cb.conjunction();

      return cb.or(predicates.toArray(Predicate[]::new));
    } catch (Exception e) {
      return cb.disjunction();
    }
  }

  public static class AcCriteriaBuilder {
    private final List<String> permissions = new ArrayList<>();
    private Requester<?> requester;

    private AcCriteriaBuilder() {}

    public AcCriteriaBuilder request(Requester<?> requester) {
      this.requester = requester;
      return this;
    }

    public AcCriteriaBuilder permission(String permission) {
      permissions.add(permission);
      return this;
    }

    public AcCriteria build() {
      return new AcCriteria(requester, permissions);
    }
  }
}
