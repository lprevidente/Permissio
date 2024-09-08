package com.lprevidente.permissio.restrictions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByHandlerEntityRestrictionTest {

  @Test
  void valid() {
    final var restriction = new AccessByHandlersRestriction("*", "handlers");

    final var user = new MockUser();
    final var requester = new Requester(0L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction = new AccessByHandlersRestriction("OP", "handlers");

    final var user = new MockUser();
    final var requester = new Requester(1L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, user)).isFalse();
  }
}
