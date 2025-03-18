package com.lprevidente.permissio.repository;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "offices")
public class Office {
  @Id private Long id;

  private String name;

  private Long creatorId;

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

  public Long getId() {
    return id;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Office office)) return false;

    return Objects.equals(id, office.id);
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
