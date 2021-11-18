package com.app.authentication.util;

import de.mkammerer.argon2.Argon2;
import org.springframework.stereotype.Component;

@Component
public class Argon2FactoryUtil {

  private Argon2 argon2 = de.mkammerer.argon2.Argon2Factory
          .create(de.mkammerer.argon2.Argon2Factory.Argon2Types.ARGON2id);

  public String passwordHash(String password) {
    char[] charArrayPassword = password.toCharArray();
    return argon2.hash(1, 1024, 1, charArrayPassword);
  }

}
