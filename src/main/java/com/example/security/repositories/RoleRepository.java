/* 13 November 2024
Spring Security: Spring Security + REST + JWT
Александр Фисунов
*/

package com.example.security.repositories;

import com.example.security.entites.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
