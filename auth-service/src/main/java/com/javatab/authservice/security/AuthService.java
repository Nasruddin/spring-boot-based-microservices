package com.javatab.authservice.security;

import com.javatab.authservice.domain.AuthRequest;
import com.javatab.authservice.domain.AuthResponse;
import com.javatab.authservice.domain.User;
import com.javatab.authservice.domain.UserVO;
import com.javatab.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        //do validation if user already exists
        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

        //UserVO userVO = restTemplate.getForObject("http://student-service/students/" + authRequest.getUsername(), UserVO.class);
        //Assert.notNull(userVO, "Failed to register user. Please try again later");

        User user = userRepository.findByUsername(authRequest.getUsername()).get();

        if (user != null) {
            String accessToken = jwtUtil.generate(user, "ACCESS");
            String refreshToken = jwtUtil.generate(user, "REFRESH");

            return new AuthResponse(accessToken, refreshToken);
        }

        return new AuthResponse(null,null);

    }
}
