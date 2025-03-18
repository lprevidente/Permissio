package com.lprevidente.permissio.repository;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

  @Id private long id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  private List<TeamMember> members;

  public Long getId() {
    return id;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Team team)) return false;

    return id == team.id;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public String toString() {
    return "Team{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
