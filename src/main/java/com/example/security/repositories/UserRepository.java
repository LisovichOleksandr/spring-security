/* 13 November 2024
Spring Security: Spring Security + REST + JWT
Александр Фисунов
*/

// Для роботы с пользователями
package com.example.security.repositories;

import com.example.security.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String username);
}

// На етом у наc готовые сущности и готовы репозитории
// Дальше нам понабодится: искать пользователей, создавать нових пользователей в случае регистрации, поэтому
// добавим слой сервисов.