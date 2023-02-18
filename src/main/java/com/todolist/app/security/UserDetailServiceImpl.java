package com.todolist.app.security;

import com.todolist.app.models.UserModel;
import com.todolist.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email" + email + "no existe."));
        return new UserDetailsImpl(user);
    }
}
