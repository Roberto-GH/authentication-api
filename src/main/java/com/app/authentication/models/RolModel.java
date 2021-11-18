package com.app.authentication.models;

import com.app.authentication.enums.RolName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Entity
public class RolModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  @NotBlank(message = "Field can't be empty")
  @Enumerated(EnumType.STRING)
  private RolName rolName;

  @Override
  public String toString() {
    return "RolModel{" +
            "id=" + id +
            ", rolName=" + rolName +
            '}';
  }

}
