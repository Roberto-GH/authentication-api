package com.app.authentication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

  private String profile_img;
  @NotBlank(message = "Field can't be empty")
  private String name;
  private String lastName;
  private String phone;
  private String biography;
  @NotBlank(message = "Field can't be empty")
  private String email;
  private LocalDateTime updateAt;

}
