package com.idle.weather.user.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.request.ReActivateRequest;
import com.idle.weather.user.api.response.UserInfoResponse;
import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.dto.AuthSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse signUp(@RequestBody AuthSignUpDto authSignUpDto) {
        return userService.signUp(authSignUpDto);
    }

    @GetMapping("/me")
    public UserInfoResponse getUserById(@UserId Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/serialId/{serialId}")
    public UserResponse getUserBySerialId(@PathVariable String serialId) {
        return userService.findBySerialId(serialId);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@UserId Long userId, @RequestBody String newPassword) {
        userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/nickname")
    public UserResponse updateNickname(@UserId Long userId, @RequestBody String newNickname) {
        return userService.updateNickname(userId, newNickname);
    }

    @GetMapping("/nickname")
    public String getNickName(@UserId Long userId) {
        return userService.getNickname(userId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@UserId Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/reactivate")
    public UserResponse reActivateUser(@RequestBody ReActivateRequest request) {
        return userService.reActivateUser(request.serialId(), request.newPassword());
    }
}