package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Group;
import com.lprevidente.permissio.entity.Handlers;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "teams")
public class Team implements BaseEntity<Long>, Group<User>, Handlers {

  @Id private long id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  private List<TeamMember> members;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  @Where(clause = "is_handler = true")
  private List<TeamMember> handlers;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public Collection<User> getMembers() {
    return members.stream().map(TeamMember::getMember).toList();
  }

  @Override
  public Collection<TeamMember> getHandlers() {
    return handlers;
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
