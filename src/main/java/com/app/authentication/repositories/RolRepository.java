package com.app.authentication.repositories;

import com.app.authentication.enums.RolName;
import com.app.authentication.models.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Integer> {

  Optional<RolModel> getByRolName(RolName rolName);

}
