package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Creatable;
import com.lprevidente.permissio.entity.Group;
import com.lprevidente.permissio.entity.HandlerEntity;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "offices")
public class Office implements BaseEntity<Long>, Group<User>, Creatable, HandlerEntity {
  @Id private Long id;

  private String name;

  private long creatorId;

  @OneToMany(mappedBy = "office")
  private List<User> members;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "office_attendees",
      joinColumns = @JoinColumn(name = "office_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> attendees;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  private User handler;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public Collection<User> getMembers() {
    return members;
  }

  @Override
  public long getCreatorId() {
    return creatorId;
  }

  @Override
  public long getHandlerId() {
    return handler != null ? handler.getId() : -1;
  }

  @Override
  public String getType() {
    return "*";
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Office office)) return false;

    return id == office.id;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public String toString() {
    return "Office{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
