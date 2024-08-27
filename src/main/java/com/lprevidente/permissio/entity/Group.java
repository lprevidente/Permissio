package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Group extends BaseEntity {

  <T extends BaseEntity> Collection<T> getMembers();
}
