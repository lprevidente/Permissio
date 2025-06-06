package com.lprevidente.permissio.repository;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "users")
public class User {

  @Id private Long id;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Handler> handlers;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  private Office office;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "office_attendees",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "office_id"))
  private Set<Office> attendanceTo;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<TeamMember> teams;

  @Override
  public String toString() {
    return "User{" + "id=" + id + '}';
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }
}
