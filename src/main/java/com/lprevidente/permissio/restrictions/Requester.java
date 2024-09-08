package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class Requester {
  protected Long id;
  protected Map<String, Restriction> permissions;

  public Requester(
      @JsonProperty("id") Long id,
      @JsonProperty("permissions") Map<String, Restriction> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Long getId() {
    return id;
  }

  public Map<String, Restriction> getPermissions() {
    return permissions;
  }

  public static class Builder {
    protected final Map<String, Restriction> permissions = new HashMap<>();
    protected long id;

    protected Builder() {}

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
