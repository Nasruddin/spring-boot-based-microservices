package com.javatab.authservice.repository;

import com.javatab.authservice.domain.Role;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
}
