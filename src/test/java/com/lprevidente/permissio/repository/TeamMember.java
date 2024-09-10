package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.HandlerEntity;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Embeddable
class TeamMemberId {

  private Long teamId;
  private Long memberId;

  public TeamMemberId() {}

  public TeamMemberId(Long teamId, Long memberId) {
    this.teamId = teamId;
    this.memberId = memberId;
  }

  public Long getTeamId() {
    return teamId;
  }

  public void setTeamId(Long teamId) {
    this.teamId = teamId;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
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

  @Override
  public String toString() {
    return "TeamMemberId{" + "teamId=" + teamId + ", memberId=" + memberId + '}';
  }
}

@Entity
@Table(name = "team_members")
public class TeamMember implements HandlerEntity<Long, String> {

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
  public Long getHandlerId() {
    return member.getId();
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TeamMember)) return false;

    TeamMember that = (TeamMember) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "TeamMember{" + "id=" + id + '}';
  }
}
