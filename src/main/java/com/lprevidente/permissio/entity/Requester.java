package com.lprevidente.permissio.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lprevidente.permissio.restriction.Restriction;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Requester<T> {
  protected T id;
  protected Map<String, Restriction> permissions;

  public Requester(
      @JsonProperty("id") T id, //
      @JsonProperty("permissions") Map<String, Restriction> permissions) {
    this.id = id;
    this.permissions = permissions;
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public T getId() {
    return id;
  }

  public Map<String, Restriction> getPermissions() {
    return permissions;
  }

  public static class Builder<T> {
    protected final Map<String, Restriction> permissions = new HashMap<>();
    protected T id;

    protected Builder() {}

    public Builder<T> id(T id) {
      this.id = id;
      return this;
    }

    public Builder<T> addPermission(String permission, Restriction restriction) {
      permissions.put(permission, restriction);
      return this;
    }

    public Requester<T> build() {
      return new Requester<>(id, permissions);
    }
  }
}
