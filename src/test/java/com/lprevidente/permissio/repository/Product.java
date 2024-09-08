package com.lprevidente.permissio.repository;

import com.lprevidente.permissio.entity.BaseEntity;
import com.lprevidente.permissio.entity.Creatable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product implements BaseEntity<String>, Creatable {
  @Id private String id;

  private long creatorId;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public long getCreatorId() {
    return creatorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Product product)) return false;
    return Objects.equals(id, product.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
