package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Creatable;
import com.lprevidente.permissio.entity.Handlers;
import com.lprevidente.permissio.entity.HandlerEntity;
import java.util.Collection;
import java.util.List;

public class MockUser implements BaseEntity<Long>, Creatable<Long>, Handlers<Long> {

  @Override
  public Long getCreatorId() {
    return 0L;
  }

  @Override
  public Long getId() {
    return 0L;
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
