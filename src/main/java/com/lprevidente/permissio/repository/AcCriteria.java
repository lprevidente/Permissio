package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.restriction.AccessByRelatedEntityRestriction;
import com.lprevidente.permissio.restriction.ConjunctionRestriction;
import com.lprevidente.permissio.restriction.DisjunctionRestriction;
import com.lprevidente.permissio.restriction.Requester;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
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
    final var join = new HashMap<String, Join<?, ?>>();
    return permissions.stream()
        .map(p -> requester.getPermissions().getOrDefault(p, new ConjunctionRestriction()))
        .map(
            r -> {
              if (r instanceof AccessByRelatedEntityRestriction relatedRestriction)
                return relatedRestriction.getRestriction().toPredicate(requester, path, cb, join);
              return cb.conjunction();
            })
        .reduce(cb::or)
        .orElse(cb.conjunction());
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
