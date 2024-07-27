package yassa_exercise.restful_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.LoginUserRequest;
import yassa_exercise.restful_api.models.ResponseData;
import yassa_exercise.restful_api.models.TokenResponse;
import yassa_exercise.restful_api.models.WebResponse;
import yassa_exercise.restful_api.service.AuthService;
import yassa_exercise.restful_api.service.UserService;

import java.util.Collections;
import java.util.Objects;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<TokenResponse>> login(@RequestBody LoginUserRequest request){
        TokenResponse tokenResponse = authService.login(request);

        ResponseData<TokenResponse> resData = new ResponseData<>();
        resData.setErrors("");
        resData.setLst(Collections.singletonList(tokenResponse));

        return WebResponse.<ResponseData<TokenResponse>>builder().success(true).data(resData).build();
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<TokenResponse>> logout(User user){
        authService.logout(user);

        return WebResponse.<ResponseData<TokenResponse>>builder().success(true).build();
    }
}
