package com.lprevidente.permissio.restrictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class Requester<ID> {
  protected ID id;
  protected Map<String, Restriction> permissions;

  public Requester(
      @JsonProperty("id") ID id,
      @JsonProperty("permissions") Map<String, Restriction> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public static <ID> Builder<ID> builder() {
    return new Builder<>();
  }

  public ID getId() {
    return id;
  }

  public Map<String, Restriction> getPermissions() {
    return permissions;
  }

  public static class Builder<ID> {
    protected final Map<String, Restriction> permissions = new HashMap<>();
    protected ID id;

    protected Builder() {}

    public Builder<ID> id(ID id) {
      this.id = id;
      return this;
    }

    public Builder<ID> addPermission(String permission, Restriction restriction) {
      permissions.put(permission, restriction);
      return this;
    }

    public Requester<ID> build() {
      return new Requester<>(id, permissions);
    }
  }
}
