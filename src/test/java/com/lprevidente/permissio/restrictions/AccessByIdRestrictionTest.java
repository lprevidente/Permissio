package com.lprevidente.permissio.restrictions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByIdRestrictionTest {

  @Test
  void valid() {
    final var restriction = new AccessByIdRestriction(0);

    final var user = new MockUser();
    final var requester = new Requester(1, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction = new AccessByIdRestriction(1L);

    final var user = new MockUser();
    final var requester = new Requester(1, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }
}
