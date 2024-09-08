package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Group<T extends BaseEntity<ID>, ID> extends BaseEntity<ID> {

  Collection<T> getMembers();
}
