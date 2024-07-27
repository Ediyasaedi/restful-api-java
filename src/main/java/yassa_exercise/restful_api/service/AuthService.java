package yassa_exercise.restful_api.service;

import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.LoginUserRequest;
import yassa_exercise.restful_api.models.ResponseData;
import yassa_exercise.restful_api.models.TokenResponse;
import yassa_exercise.restful_api.models.WebResponse;
import yassa_exercise.restful_api.repository.UserRepository;
import yassa_exercise.restful_api.security.BCrypt;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatorService validatorService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validatorService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is incorrect"));

        if(BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiresAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
    }

    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(0);
        userRepository.save(user);
    }

    private long next30Days(){
        return System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000;
    }
}
