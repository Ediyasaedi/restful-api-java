package yassa_exercise.restful_api.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.UpdateUserRequest;
import yassa_exercise.restful_api.models.UserResponse;
import yassa_exercise.restful_api.models.RegisterUserRequest;
import yassa_exercise.restful_api.repository.UserRepository;
import yassa_exercise.restful_api.security.BCrypt;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private ValidatorService validatorService;

    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        validatorService.validate(request);

        if(userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());
        userRepository.save(user);

        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();

    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request){
        validatorService.validate(request);

        if(Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if(Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);
        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }
}
