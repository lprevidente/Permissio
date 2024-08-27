package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Group;
import java.util.Collection;
import java.util.List;

public class MockTeam implements BaseEntity, Group {

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public Collection<BaseEntity> getMembers() {
    return List.of(new MockUser());
  }
}
