package com.idle.weather.user.api;

import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로그를 위한 추가
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Slf4j // 로그 어노테이션 추가
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> signUp(@RequestBody AuthSignUpDto authSignUpDto) {
        UserEntity newUser = userService.signUp(authSignUpDto);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serialId/{serialId}")
    public ResponseEntity<UserEntity> getUserBySerialId(@PathVariable String serialId) {
        return userService.findBySerialId(serialId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}