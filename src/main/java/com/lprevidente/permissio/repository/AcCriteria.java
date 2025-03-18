package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.Relatable;
import com.lprevidente.permissio.restriction.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AcCriteria {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Requester<?> requester;
  private final List<String> permissions;

  private AcCriteria(Requester<?> requester, List<String> permissions) {
    Assert.notNull(requester, "The requester cannot be null");
    Assert.notNull(permissions, "The permissions cannot be null");

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
    final var restrictions =
        permissions.stream()
            .map(p -> requester.getPermissions().getOrDefault(p, new Disjunction()))
            .toList();

    if (restrictions.stream().anyMatch(Conjunction.class::isInstance)) return cb.conjunction();

    if (restrictions.stream()
        .filter(Or.class::isInstance)
        .map(o -> ((Or) o).restrictions())
        .anyMatch(Conjunction.class::isInstance)) return cb.conjunction();

    return restrictions.stream()
        .map(r -> r.toPredicate(requester, path, cb, join))
        .reduce(cb::or)
        .orElse(cb.conjunction());
  }

  public Predicate getPredicateRelated(Path<?> path, CriteriaBuilder cb) {
    try {
      final var join = new HashMap<String, Join<?, ?>>();
      final var predicates = new ArrayList<Predicate>();

      final var obj = path.getJavaType().getConstructor().newInstance();
      if (!(obj instanceof Relatable)) return cb.disjunction();

      final var getKeyJoin = path.getJavaType().getDeclaredMethod("getKeyJoin", String.class);

      for (var p : permissions) {
        final var key = getKeyJoin.invoke(obj, p);
        if (key == null) continue;

        final var restriction = requester.getPermissions().getOrDefault(p, new Conjunction());
        if (restriction instanceof Or or)
          Arrays.stream(or.restrictions())
              .map(r -> toPredicateRelated(r, key, path, cb, join))
              .filter(Objects::nonNull)
              .forEach(predicates::add);
        else if (restriction instanceof AccessByRelatedEntity r && r.property().equals(key))
          predicates.add(r.getRestriction().toPredicate(requester, path, cb, join));
      }

      if (predicates.isEmpty()) return cb.conjunction();
      return cb.or(predicates.toArray(Predicate[]::new));
    } catch (Exception exception) {
      log.warn("Exception occurred while getting key join", exception);
      return cb.disjunction();
    }
  }

  private Predicate toPredicateRelated(
      Restriction restriction,
      Object key,
      Path<?> path,
      CriteriaBuilder cb,
      Map<String, Join<?, ?>> join) {
    if (restriction instanceof Conjunction) return cb.conjunction();
    if (restriction instanceof Disjunction) return cb.disjunction();

    if (restriction instanceof AccessByRelatedEntity rs && rs.getRestriction().equals(key))
      return rs.getRestriction().toPredicate(requester, path, cb, join);
    return null;
  }

  public static class AcCriteriaBuilder {
    private final List<String> permissions = new ArrayList<>();
    private Requester requester;

    private AcCriteriaBuilder() {}

    public AcCriteriaBuilder request(Requester requester) {
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
