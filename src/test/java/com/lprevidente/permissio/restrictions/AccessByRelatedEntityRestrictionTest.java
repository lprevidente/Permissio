package com.lprevidente.permissio.restrictions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByRelatedEntityRestrictionTest {

  @Test
  void valid() {
    final var restriction =
        new AccessByRelatedEntityRestriction("office", new AccessByIdRestriction(0));

    final var user = new MockUser();
    final var requester = new Requester(0, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction =
        new AccessByRelatedEntityRestriction("office", new AccessByIdRestriction(1));

    final var user = new MockUser();
    final var requester = new Requester(1, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }

  @Test
  void validList() {
    final var restriction =
        new AccessByRelatedEntityRestriction("teams", new AccessByIdRestriction(0));

    final var user = new MockUser();
    final var requester = new Requester(0, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValidList() {
    final var restriction =
        new AccessByRelatedEntityRestriction("teams", new AccessByIdRestriction(1));

    final var user = new MockUser();
    final var requester = new Requester(1, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }
}
