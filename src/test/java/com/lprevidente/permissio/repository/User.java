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
public class User implements BaseEntity, Creatable, Handlers {

  @Id private long id;

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
  public long getCreatorId() {
    if (creator == null) return -1;
    return creator.getId();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + '}';
  }

  @Override
  public Collection<Handler> getHandlers() {
    return handlers;
  }
}
