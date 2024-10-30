package com.crimeprevention.smartsurveillancesystem.repositories;


import com.crimeprevention.smartsurveillancesystem.models.PasswordResetToken;
import com.crimeprevention.smartsurveillancesystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}
