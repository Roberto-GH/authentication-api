package com.app.authentication.models;

import com.app.authentication.jwt.JwtProvider;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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
  private String tokenPassword;
  private LocalDateTime createdAt;
  private LocalDateTime updateAt;
  private LocalDateTime lastLogin;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "user_id"),
  inverseJoinColumns = @JoinColumn(name = "rol_id"))
  private Set<RolModel> roles;
  @OneToMany(targetEntity = RefreshTokenModel.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "id")
  private List<RefreshTokenModel> refreshTokens;


  public void addRefreshToken(RefreshTokenModel refreshToken){
    refreshTokens.add(refreshToken);
    refreshToken.setUser(this);
  }

  private final static Logger _log = LoggerFactory.getLogger(JwtProvider.class);

  public void removeRefreshToken(RefreshTokenModel refreshToken){

//    _log.info("size"+this.refreshTokens.size());
//
//    boolean isDeleted = refreshTokens.remove(refreshToken);
//    _log.error("is deleted: "+ isDeleted);
    refreshToken.setUser(null);
  }


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
