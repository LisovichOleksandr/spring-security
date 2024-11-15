/* 13 November 2024
Spring Security: Spring Security + REST + JWT
Александр Фисунов
*/

// Начинаем с ролей потому что ЮЗЕРЫ на ролях завязаны
package com.example.security_ed.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}

/* В ролях ентіті дата і тейбл
* два поля ай-ді і ім'я
* GenerationType.IDENTITY
* @GeneratedValue стратегия
*  */