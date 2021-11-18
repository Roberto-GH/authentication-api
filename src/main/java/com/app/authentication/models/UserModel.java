package com.app.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  private String profile_img;
  @NotBlank(message = "Field can't be empty")
  private String name;
  private String lastName;
  @Lob
  private String biography;
  private String phone;
  @NotBlank(message = "Field can't be empty")
  @Email(message = "Field will be a valid email")
  @Column(unique = true)
  private String email;
  @NotBlank(message = "Field can't be empty")
  @Length(min=6, message="Password don't have a valid format")
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "user_id"),
  inverseJoinColumns = @JoinColumn(name = "rol_id"))
  private Set<RolModel> roles = new HashSet<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserModel userModel = (UserModel) o;
    return email != null && Objects.equals(email, userModel.email);
  }

  @Override
  public int hashCode() {
    return id;
  }

}
