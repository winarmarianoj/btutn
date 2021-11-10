package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(Roles nameRole);
}
