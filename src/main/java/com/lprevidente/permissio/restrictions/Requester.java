package com.lprevidente.permissio.restrictions;

import java.util.Map;

public class Requester {
  private final long id;
  private final Map<String, Restriction> permissions;

  public Requester(long id, Map<String, Restriction> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public long getId() {
    return id;
  }

  public Map<String, Restriction> getPermissions() {
    return permissions;
  }
}
