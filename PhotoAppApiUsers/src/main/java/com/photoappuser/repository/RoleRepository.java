package com.photoappuser.repository;

import com.photoappuser.model.Role;
import com.photoappuser.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role , Long>{

    Optional<Role> findByName(RoleName name);
}
