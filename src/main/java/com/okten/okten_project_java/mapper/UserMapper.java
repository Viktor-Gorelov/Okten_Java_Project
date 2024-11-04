package com.okten.okten_project_java.mapper;

import com.okten.okten_project_java.dto.user.UserDTO;
import com.okten.okten_project_java.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO mapToDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .userStatus(user.getUserStatus())
                .isBanned(user.getIsBanned())
                .userRole(user.getUserRole())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }

    public User mapToUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setUserStatus(userDTO.getUserStatus());
        user.setIsBanned(userDTO.getIsBanned());
        user.setUserRole(userDTO.getUserRole());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
