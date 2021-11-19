package com.app.authentication.repositories;

import com.app.authentication.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

  Optional<UserModel> getByName(String name);
  Optional<UserModel> getByEmail(String email);
  Optional<UserModel> getByTokenPassword(String tokenPassword);
  boolean existsByName(String name);
  boolean existsByEmail(String email);
  boolean existsByTokenPassword(String tokenPassword);

}
