package com.dalila.blog.auth;

import com.dalila.blog.entities.Role;
import com.dalila.blog.entities.User;
import com.dalila.blog.repository.UserRepository;
import com.dalila.blog.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request){
        User user = new User(
                null,
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.ADMIN);
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();

    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request){
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getEmail(),
                           request.getPassword()
                   )
           );
           var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found."));
           var jwt = jwtService.generateToken(user);
           return AuthenticationResponse.builder().token(jwt).build();

       }catch(Exception e){
           e.printStackTrace();
           throw new RuntimeException("Invalid login credentials", e);
       }

    }
}
