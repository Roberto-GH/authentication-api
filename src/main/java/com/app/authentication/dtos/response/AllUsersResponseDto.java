package com.app.authentication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersResponseDto {

  private long id;
  private String profile_img;
  private String name;
  private String lastName;

}
