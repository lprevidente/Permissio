package com.lprevidente.permissio.restriction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByIdRestrictionTest {

  @Test
  void valid() {
    final var restriction = new AccessByIdRestriction<>(0L);

    final var user = new MockUser();
    final var requester = new Requester(1L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction = new AccessByIdRestriction<>(1L);

    final var user = new MockUser();
    final var requester = new Requester(1L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }
}
