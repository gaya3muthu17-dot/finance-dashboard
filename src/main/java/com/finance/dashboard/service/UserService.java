package com.finance.dashboard.service;

import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository r, PasswordEncoder e) { this.userRepository = r; this.passwordEncoder = e; }

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public Optional<User> getById(Long id) { return userRepository.findById(id); }
    public Optional<User> getByEmail(String email) { return userRepository.findByEmail(email); }

    public User save(User user) {
        if (user.getId() == null) user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update without re-encoding password (password already set correctly)
    public User updateProfile(User user) { return userRepository.save(user); }

    public void delete(Long id) { userRepository.deleteById(id); }
    public boolean existsByEmail(String email) { return userRepository.existsByEmail(email); }
}
