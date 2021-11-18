package com.app.authentication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto implements Serializable {

  private String profile_img;
  @NotBlank(message = "Field can't be empty")
  private String name;
  private String lastName;
  private String phone;
  @NotBlank(message = "Field can't be empty")
  @Email(message = "Field will be a valid email")
  private String email;
  @NotBlank(message = "Field can't be empty")
  @Length(min=6, message="Password don't have a valid format")
  private String password;
  private LocalDateTime createdAt;
  private Set<String> roles = new HashSet<>();

}
