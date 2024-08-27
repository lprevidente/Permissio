package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.HandlerEntity;

public class MockUserHandlerEntity implements HandlerEntity {

  @Override
  public String getType() {
    return "HR";
  }

  @Override
  public long getHandlerId() {
    return 0;
  }

}
