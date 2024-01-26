package com.productiontracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.productiontracking.entity.User;
import com.productiontracking.repository.UserRepository;
import com.productiontracking.security.JwtUserDetails;

@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository _userRepository;

    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        User user = _userRepository.findByEmail(username);
        if (user.getStatus().equals(User.Status.INACTIVE.toString())) {
            throw new AuthenticationException("User is inactive") {
            };
        }
        return JwtUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = _userRepository.findById(id).get();
        return JwtUserDetails.create(user);
    }

}
