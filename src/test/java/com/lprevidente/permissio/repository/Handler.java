package com.lprevidente.permissio.repository;

import static org.hibernate.annotations.OnDeleteAction.*;

import com.lprevidente.permissio.entity.HandlerEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;

class HandlerId implements Serializable {
  private long id;
  private String type;

  public HandlerId() {}

  public HandlerId(long id, String type) {
    this.id = id;
    this.type = type;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    HandlerId that = (HandlerId) obj;
    return id == that.id && Objects.equals(type, that.type);
  }

  @Override
  public String toString() {
    return "HandlerId{" + "id=" + id + ", type='" + type + '\'' + '}';
  }
}

@Entity
@Table(name = "handlers")
@IdClass(HandlerId.class)
public class Handler implements HandlerEntity {

  @Id private long id;

  @Id private String type;

  @ManyToOne
  @MapsId("id")
  @JoinColumn(name = "id")
  @OnDelete(action = CASCADE)
  private User handler;

  @ManyToOne
  @OnDelete(action = CASCADE)
  private User user;

  @Override
  public long getHandlerId() {
    return id;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Handler handler)) return false;

    return id == handler.id;
  }

  @Override
  public int hashCode() {
    int result = Long.hashCode(id);
    result = 31 * result + Objects.hashCode(type);
    return result;
  }

  @Override
  public String toString() {
    return "Handler{" + "id=" + id + ", type='" + type + '\'' + '}';
  }
}
