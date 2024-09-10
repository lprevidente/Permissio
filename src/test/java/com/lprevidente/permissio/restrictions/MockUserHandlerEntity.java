package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.HandlerEntity;

public class MockUserHandlerEntity implements HandlerEntity<Long> {

  @Override
  public String getType() {
    return "HR";
  }

  @Override
  public Long getHandlerId() {
    return 0L;
  }
}
