package com.lprevidente.permissio.restriction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AccessByMemberRestrictionTest {

  @Test
  void valid() {
    final var restriction = new AccessByMemberRestriction<MockUser>("members");

    final var team = new MockTeam();
    final var requester = new Requester(0L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, team)).isTrue();
  }

  @Test
  void notValid() {
    final var restriction = new AccessByMemberRestriction<MockUser>("members");

    final var team = new MockTeam();
    final var requester = new Requester(1L, Map.of());

    assertThat(restriction.isSatisfiedBy(requester, team)).isFalse();
  }
}
