package com.lprevidente.permissio.restrictions;

import com.lprevidente.permissio.entity.BaseEntity;

public class MockOffice implements BaseEntity<Long> {

  @Override
  public Long getId() {
    return 0L;
  }
}
