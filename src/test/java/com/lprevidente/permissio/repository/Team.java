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
public class Team implements BaseEntity, Group, Handlers {

  @Id private long id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  private List<TeamMember> members;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  @Where(clause = "is_handler = true")
  private List<TeamMember> handlers;

  @Override
  public long getId() {
    return id;
  }

  @Override
  public Collection<User> getMembers() {
    return members.stream().map(TeamMember::getMember).toList();
  }

  @Override
  public String toString() {
    return "Team{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public Collection<TeamMember> getHandlers() {
    return handlers;
  }
}
