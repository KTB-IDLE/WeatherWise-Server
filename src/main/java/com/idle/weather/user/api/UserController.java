package com.idle.weather.user.api;

import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.request.ReActivateRequest;
import com.idle.weather.user.api.response.UserResponse;
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
    public UserResponse signUp(@RequestBody AuthSignUpDto authSignUpDto) {
        return userService.signUp(authSignUpDto);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/serialId/{serialId}")
    public UserResponse getUserBySerialId(@PathVariable String serialId) {
        return userService.findBySerialId(serialId);
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @RequestBody String newPassword) {
        userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/nickname/{userId}")
    public UserResponse updateNickname(@PathVariable Long userId, @RequestBody String newNickname) {
        return userService.updateNickname(userId, newNickname);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/reactivate")
    public UserResponse reActivateUser(@RequestBody ReActivateRequest request) {
        return userService.reActivateUser(request.serialId(), request.newPassword());
    }
}