package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.HandlerEntity;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Embeddable
class TeamMemberId {

  private long teamId;
  private long memberId;

  public TeamMemberId() {}

  public TeamMemberId(long teamId, long memberId) {
    this.teamId = teamId;
    this.memberId = memberId;
  }

  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  public long getMemberId() {
    return memberId;
  }

  public void setMemberId(long memberId) {
    this.memberId = memberId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TeamMemberId that = (TeamMemberId) o;
    return teamId == that.teamId && memberId == that.memberId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, memberId);
  }
}

@Entity
@Table(name = "team_members")
public class TeamMember implements HandlerEntity {

  @EmbeddedId private TeamMemberId id;

  @ManyToOne
  @MapsId("memberId")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User member;

  @ManyToOne
  @MapsId("teamId")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Team team;

  private boolean isHandler;

  private String type;

  public User getMember() {
    return member;
  }

  @Override
  public String getType() {
    return "*";
  }

  @Override
  public long getHandlerId() {
    return member.getId();
  }
}
