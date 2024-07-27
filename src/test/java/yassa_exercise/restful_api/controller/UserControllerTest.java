package yassa_exercise.restful_api.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.*;
import yassa_exercise.restful_api.repository.UserRepository;
import yassa_exercise.restful_api.security.BCrypt;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("testUsername");
        registerUserRequest.setPassword("testPassword");
        registerUserRequest.setName("Test Name");
        mockMvc.perform(post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserRequest)))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ResponseData<Object>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    System.out.println(response.toString());
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("");
        registerUserRequest.setPassword("");
        registerUserRequest.setName("");
        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest)))
                .andExpectAll(
                        status().isBadRequest()
                ).andDo(result -> {
                    WebResponse<ResponseData<Object>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ResponseData<Object>>>() {});
                    assertEquals("Is Success Response : ", false, response.isSuccess());
                });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword(BCrypt.hashpw("testPassword", BCrypt.gensalt()));
        user.setName("Test Name");
        userRepository.save(user);

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("testUsername");
        registerUserRequest.setPassword("testPassword");
        registerUserRequest.setName("Test Name");
        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest)))
                .andExpectAll(
                        status().isBadRequest()
                ).andDo(result -> {
                    WebResponse<ResponseData<Object>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ResponseData<Object>>>() {});
                    assertEquals("Is Success Response : ", false, response.isSuccess());
                });
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/user/current").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<Object>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ResponseData<Object>>>() {});
            assertEquals("Is Success Response : ", false, response.isSuccess());
        });
    }

    @Test
    void getUserWithoutToken() throws Exception {
        mockMvc.perform(
                get("/api/user/current").accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<Object>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ResponseData<Object>>>() {});
            assertEquals("Is Success Response : ", false, response.isSuccess());
        });
    }

    @Test
    void getUserWithoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword(BCrypt.hashpw("testPassword", BCrypt.gensalt()));
        user.setName("Test Name");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/user/current").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ResponseData<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("Is Success Response : ", true, response.isSuccess());
            assertEquals("", "testUsername", response.getData().getLst().getFirst().getUsername());
            assertEquals("", "Test Name", response.getData().getLst().getFirst().getName());
        });
    }

    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword(BCrypt.hashpw("testPassword", BCrypt.gensalt()));
        user.setName("Test Name");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 10000000000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/user/current").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("Is Success Response : ", false, response.isSuccess());
        });
    }

    @Test
    void patchUserUpdateUnauthorized() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/user/current").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("Is Success Response : ", true, response.isSuccess());
            assertEquals("", "Edi Yasa", response.getData().getLst().getFirst().getName());
            assertEquals("", "testUsername", response.getData().getLst().getFirst().getUsername());
        });
    }

    @Test
    void patchUserUpdateSuccess() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword(BCrypt.hashpw("testPassword", BCrypt.gensalt()));
        user.setName("Test Name");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000000L);
        userRepository.save(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Edi Yasa");
        updateUserRequest.setPassword("password");

        mockMvc.perform(
                patch("/api/user/current").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ResponseData<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("Is Success Response : ", true, response.isSuccess());

            User userDB = userRepository.findById("testUsername").orElse(null);
            Assertions.assertNotNull(userDB);
            assertTrue(BCrypt.checkpw("password", userDB.getPassword()));
        });
    }
}