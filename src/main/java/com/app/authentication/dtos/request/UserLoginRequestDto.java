package com.app.authentication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {

  @NotBlank(message = "Field can't be empty")
  @Email(message = "Field will be a valid email")
  private String email;
  @NotBlank(message = "Field can't be empty")
  @Length(min=6, message="Password don't have a valid format")
  private String password;

}
