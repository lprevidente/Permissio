package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.restrictions.Requester;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

public class Specification {
  private final Requester requester;
  private final List<String> permissions;

  private Specification(Requester requester, List<String> permissions) {
    Assert.notNull(requester, "The requester cannot be null");
    this.permissions = permissions;
    this.requester = requester;
  }

  public static SpecificationBuilder builder() {
    return new SpecificationBuilder();
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public Requester getRequester() {
    return requester;
  }

  public static class SpecificationBuilder {
    private final List<String> permissions = new ArrayList<>();
    private Requester requester;

    private SpecificationBuilder() {}

    public SpecificationBuilder request(Requester userPrincipal) {
      this.requester = userPrincipal;
      return this;
    }

    public SpecificationBuilder permission(String permission) {
      permissions.add(permission);
      return this;
    }

    public Specification build() {
      return new Specification(requester, permissions);
    }
  }
}
