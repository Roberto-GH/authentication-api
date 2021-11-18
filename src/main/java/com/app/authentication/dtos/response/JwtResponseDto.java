package com.app.authentication.dtos.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponseDto {

  private String token;
  //private String bearer = "Bearer";
  //private String email;
  //private Collection<? extends GrantedAuthority> authorities;

  public JwtResponseDto(String token) {
    this.token = token;
  }
  /*
  public JwtResponseDto(String token, String email, Collection<? extends GrantedAuthority> authorities) {
    this.token = token;
    this.email = email;
    this.authorities = authorities;
  }
   */

}
