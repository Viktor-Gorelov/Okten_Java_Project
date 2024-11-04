package com.okten.okten_project_java.services;

import com.okten.okten_project_java.dto.auth.SignUpRequestDTO;
import com.okten.okten_project_java.dto.auth.SignUpResponseDTO;
import com.okten.okten_project_java.dto.user.UserDTO;
import com.okten.okten_project_java.dto.user.UserUpdateDTO;
import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.enums.UserStatus;
import com.okten.okten_project_java.mapper.UserMapper;
import com.okten.okten_project_java.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);

    }
    public List<UserDTO> getAllUsers(){
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    @Transactional
    public SignUpResponseDTO createAuthorizeUser(SignUpRequestDTO signUpRequestDto, String userRole) {
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .password(password)
                .userRole(userRole)
                .isBanned(false)
                .userStatus(UserStatus.STANDARD)
                .build();
        User savedUser = userRepository.save(user);

        return SignUpResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .registeredAt(savedUser.getRegisteredAt())
                .build();
    }

    public UserDTO getUserById(Long id){
        User user = findUser(id);
        return userMapper.mapToDTO(user);
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO){
        User user = findUser(id);
        user.setUsername(userUpdateDTO.getUsername());
        user.setPassword(userUpdateDTO.getPassword());
        user.setEmail(userUpdateDTO.getEmail());
        User savedUser = userRepository.save(user);
        return userMapper.mapToDTO(savedUser);
    }

    public void banUser(Long id){
        changeBanStatus(id,true);
    }

    public void unBanUser(Long id){
        changeBanStatus(id,false);
    }

    private void changeBanStatus(Long id, boolean banStatus){
        User user = findUser(id);
        user.setIsBanned(banStatus);
        userRepository.save(user);
    }

    private User findUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user with this id isn't present"));
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }
}
