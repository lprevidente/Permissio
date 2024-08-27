package com.lprevidente.permissio.restrictions;

import java.util.HashMap;
import java.util.Map;

public class Requester {
  private final long id;
  private final Map<String, Restriction> permissions;

  public Requester(long id, Map<String, Restriction> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public static Builder builder() {
    return new Builder();
  }

  public long getId() {
    return id;
  }

  public Map<String, Restriction> getPermissions() {
    return permissions;
  }

  public static class Builder {
    private final Map<String, Restriction> permissions = new HashMap<>();
    private long id;

    private Builder() {}

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder addPermission(String permission, Restriction restriction) {
      permissions.put(permission, restriction);
      return this;
    }

    public Requester build() {
      return new Requester(id, permissions);
    }
  }
}
