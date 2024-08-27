package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.restrictions.Requester;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

public class Specification {
  private final Requester request;
  private final List<String> permissions;

  private Specification(Requester request, List<String> permissions) {
    Assert.notNull(request, "The requester cannot be null");
    this.permissions = permissions;
    this.request = request;
  }

  public static SpecificationBuilder builder() {
    return new SpecificationBuilder();
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public Requester getRequester() {
    return request;
  }

  public static class SpecificationBuilder {
    private final List<String> permissions = new ArrayList<>();
    private Requester requester;

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
