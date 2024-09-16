package com.lprevidente.permissio.restriction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByCreatorRestrictionTest {

  @Test
  void valid() {
    final var restriction = new AccessByCreatorRestriction("creatorId");

    final var user = new MockUser();
    final var requester = new Requester(0L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction = new AccessByCreatorRestriction("creatorId");

    final var user = new MockUser();
    final var requester = new Requester(1L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }
}
