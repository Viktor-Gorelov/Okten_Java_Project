package com.okten.okten_project_java.repositories;

import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.entities.UserRole;
import com.okten.okten_project_java.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findUserByUserRole(String userRole);

}
