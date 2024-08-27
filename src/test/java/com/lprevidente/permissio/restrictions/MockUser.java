package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Creatable;
import com.lprevidente.permissio.entity.Handlers;
import com.lprevidente.permissio.entity.HandlerEntity;
import java.util.Collection;
import java.util.List;

public class MockUser implements BaseEntity, Creatable, Handlers {

  @Override
  public long getCreatorId() {
    return 0;
  }

  @Override
  public long getId() {
    return 0;
  }

  public MockOffice getOffice() {
    return new MockOffice();
  }

  public List<MockTeam> getTeams() {
    return List.of(new MockTeam());
  }

  @Override
  public Collection<HandlerEntity> getHandlers() {
    return List.of(new MockUserHandlerEntity());
  }
}
