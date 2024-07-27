package yassa_exercise.restful_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.*;
import yassa_exercise.restful_api.service.UserService;

import java.util.Collections;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<UserResponse>> register(@RequestBody RegisterUserRequest request) {
        UserResponse user = userService.register(request);

        ResponseData<UserResponse> resData = new ResponseData<>();
        resData.setErrors("");
        resData.setLst(Collections.singletonList(user));

        return WebResponse.<ResponseData<UserResponse>>builder().success(true).data(resData).build();
    }

    @GetMapping(
            path = "/api/user/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<UserResponse>> get(User user){
        UserResponse userResponse = userService.get(user);

        ResponseData<UserResponse> resData = new ResponseData<>();
        resData.setErrors("");
        resData.setLst(Collections.singletonList(userResponse));
        return WebResponse.<ResponseData<UserResponse>>builder().success(true).data(resData).build();
    }

    @PatchMapping(
            path = "/api/user/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ResponseData<UserResponse>> update(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(user, request);

        ResponseData<UserResponse> resData = new ResponseData<>();
        resData.setErrors("");
        resData.setLst(Collections.singletonList(userResponse));

        return WebResponse.<ResponseData<UserResponse>>builder().success(true).data(resData).build();
    }
}
