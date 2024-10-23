package com.crimeprevention.smartsurveillancesystem.repositories;

import com.crimeprevention.smartsurveillancesystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

;import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
    User getUserByEmail(String email);
}
