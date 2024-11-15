//| Класс которий роботает с токенами

package com.example.security_ed.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    // Для начала нужен метод которий из пользователя сформирует токен
    // Я могу взять своего юзера , а могу стандартного от СПРИНГА
    public String generateToken(UserDetails userDetails){
        // Нужно как то формировать ПАЙ_ЛОАД
        Map<String, Object> claims = new HashMap<>();  //claims - претензии
        // Нужно в токен подшить списо ролей
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()); // Преобразовали к набору строк
        claims.put("roles", roleList);
//        claims.put("email", user.getEmail); - Если ето бил бы наш ЮЗЕР (можно положить что угодно)
        // Дальше у токена должна бить информация о том когда он создан и когда истечет время его жизни
        Date issuedDate = new Date(); //текущее время   // issued-изданный
        // время истечет начиная от момента создания токена, + jwtLifetime.toMillis() - из ПРОПЕРТИ файла
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());   // expired-истекший
        //Дальше мы хотим создать токен
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)    // Нужно токен подписать
                .compact();
    }
    // Впринципе метод генерации токена уже есть (он хороший)
    // Потихоньку можем двигаться вводить логин пароль получать по нему токен, как только пропишим контроллер,
    // которий будет получать от пользователя логин, пароль
    // Но у нас будут методы когда клиент нам присылает запос, а нам нужно провероить
    // "А кто ето такой?, Как его зовут?, Какой у него список ролей?"
    // Нам нужно уметь розбирать токен на куски
    private Claims getAllClaimsFromToken(String token){
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser()
                .setSigningKey(secretKey)
                // Когда мы хотим из токена что-то дастать, то указываем СЕКРЕТНЫЙ ключ
                // если вдруг кто-то подменит данние то на етом етапе получим ошибку
                .parseClaimsJws(token)
                .getBody();
    }
    // Ето общий метод но мы хотим конкретики, поетому мы сделаем еще два метода
    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject(); // Имя пользователя будет жить в Subject-е
    }

    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
}

// Наш утилитний класс готов, он умеет по данным от пользователя Формировать токен, а также он умеет доставать из
// приходящего к нам токена и имя и список ролей
// --/-- дальше научимся выдавать пользователю токен, для етого на монадобится еще один контроллер AuthController




















