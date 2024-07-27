package yassa_exercise.restful_api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.LoginUserRequest;
import yassa_exercise.restful_api.models.ResponseData;
import yassa_exercise.restful_api.models.TokenResponse;
import yassa_exercise.restful_api.models.WebResponse;
import yassa_exercise.restful_api.repository.UserRepository;
import yassa_exercise.restful_api.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

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
    void loginFailedNotNull() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin");
        loginUserRequest.setPassword("admin");

        mockMvc.perform(
                post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<TokenResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });

            assertNotNull(response.getData().getErrors());
        });
    }

    @Test
    void loginFailedPasswordIncorrect() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        user.setName("admin");
        userRepository.save(user);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin");
        loginUserRequest.setPassword("admin111");

        mockMvc.perform(
                post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<TokenResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });

            assertNotNull(response.getData().getErrors());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        user.setName("admin");
        userRepository.save(user);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("admin");
        loginUserRequest.setPassword("admin");

        mockMvc.perform(
                post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ResponseData<TokenResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });

            assertEquals("", response.getData().getErrors());
            assertNotNull(response.getData().getLst().getFirst().getToken());
            assertNotNull(response.getData().getLst().getFirst().getExpiresAt());

            User userDB = userRepository.findById(user.getUsername()).orElse(null);
            assertNotNull(userDB);
            assertEquals(response.getData().getLst().getFirst().getToken(), userDB.getToken());
            assertEquals(response.getData().getLst().getFirst().getExpiresAt(), userDB.getTokenExpiredAt());
        });
    }

    @Test
    void logoutFailedN() throws Exception {
        mockMvc.perform(
                delete("/api/auth/logout").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<ResponseData<TokenResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });

            assertNotNull(response.getData().getErrors());
        });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        user.setName("admin");
        user.setToken("testToken");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/auth/logout").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "testToken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ResponseData<TokenResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });

            assertTrue(response.isSuccess());

            User userDB = userRepository.findById(user.getUsername()).orElse(null);
            assertNotNull(userDB);
            assertNull(userDB.getToken());
            assertEquals(0, userDB.getTokenExpiredAt());
        });
    }
}