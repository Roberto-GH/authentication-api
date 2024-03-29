package com.app.authentication.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationAuthenticationApp {

  @Bean
  public static AmazonS3Client amazonS3Client() {
    return (AmazonS3Client) AmazonS3ClientBuilder
      .standard()
      .withCredentials(new DefaultAWSCredentialsProviderChain())
      .build();
  }

}
