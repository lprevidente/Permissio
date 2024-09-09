package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Group<T extends BaseEntity<?>> {

  Collection<T> getMembers();
}
