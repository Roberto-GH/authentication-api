package com.app.authentication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailValuesDto {

  private String mailFrom;
  private String mailTo;
  private String subject;
  private String userName;
  private String tokenPassword;

}
