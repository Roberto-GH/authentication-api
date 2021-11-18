package com.app.authentication.dtos.response;

import com.app.authentication.models.RolModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto implements Serializable {

  private int id;
  private String profile_img;
  private String name;
  private String lastName;
  private String biography;
  private String phone;
  private String email;
  private Set<RolModel> roles = new HashSet<>();

}
