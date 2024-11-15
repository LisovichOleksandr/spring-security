/* 13 November 2024
Spring Security: Spring Security + REST + JWT
Александр Фисунов
*/

package com.example.security_ed.repositories;

import com.example.security_ed.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
