package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Creatable;
import com.lprevidente.permissio.entity.Handlers;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "users")
public class User implements BaseEntity<Long>, Creatable<Long>, Handlers<Long> {

  @Id private Long id;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Handler> handlers;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  private Office office;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
  private List<TeamMember> teams;

  @Override
  public Long getCreatorId() {
    if (creator == null) return -1L;
    return creator.getId();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + '}';
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return id == user.id;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public Collection<Handler> getHandlers() {
    return handlers;
  }
}
