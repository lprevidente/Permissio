package com.lprevidente.permissio.restriction;

import com.lprevidente.permissio.entity.HandlerEntity;

public class MockUserHandlerEntity implements HandlerEntity<Long, String> {

  @Override
  public String getType() {
    return "HR";
  }

  @Override
  public Long getHandlerId() {
    return 0L;
  }
}
