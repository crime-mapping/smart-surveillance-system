package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.User;
import com.crimeprevention.smartsurveillancesystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUser(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User emailExists(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User updateUser(long id, User user) {
        User updatedUser = null;
        if (userRepository.existsById(id)) {
            user.setId(id);
             updatedUser = userRepository.save(user);
        }
        return  updatedUser;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

}