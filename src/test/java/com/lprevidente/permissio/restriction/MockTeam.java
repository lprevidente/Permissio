package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Group;
import java.util.Collection;
import java.util.List;

public class MockTeam implements BaseEntity<Long>, Group<MockUser> {

  @Override
  public Long getId() {
    return 0L;
  }

  @Override
  public Collection<MockUser> getMembers() {
    return List.of(new MockUser());
  }
}
