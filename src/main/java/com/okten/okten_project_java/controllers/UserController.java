package com.okten.okten_project_java.controllers;

import com.okten.okten_project_java.dto.user.UserDTO;
import com.okten.okten_project_java.dto.user.UserUpdateDTO;
import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id,
                                                  @RequestBody UserUpdateDTO userUpdateDTO){
        return ResponseEntity.ok(userService.updateUser(id,userUpdateDTO));
    }

    @PutMapping("/ban/{id}")
    public ResponseEntity<Void> banUserById(@PathVariable Long id){
        userService.banUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unban/{id}")
    public ResponseEntity<Void> unBanUserById(@PathVariable Long id){
        userService.unBanUser(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
