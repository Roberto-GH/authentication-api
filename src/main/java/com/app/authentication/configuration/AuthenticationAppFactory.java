package com.app.authentication.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationAppFactory {

  @Bean
  public ModelMapper modelMapper(){
    return new ModelMapper();
  }

}
