package yassa_exercise.restful_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import yassa_exercise.restful_api.entity.Contact;
import yassa_exercise.restful_api.entity.User;
import yassa_exercise.restful_api.models.*;
import yassa_exercise.restful_api.repository.ContactRepository;
import yassa_exercise.restful_api.repository.UserRepository;
import yassa_exercise.restful_api.security.BCrypt;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("username");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("tokenTest");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setFirstName("");
        createContactRequest.setEmail("salah");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContactRequest))
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isBadRequest()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                    assertNotNull(response.getData().getErrors());
                });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setFirstName("Edi");
        createContactRequest.setLastName("Yasa");
        createContactRequest.setEmail("edi@gmail.com");
        createContactRequest.setPhoneNumber("08191179657");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContactRequest))
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                    assertEquals("FirstName", "Edi", response.getData().getLst().getFirst().getFirstName());
                    assertEquals("LastName", "Yasa", response.getData().getLst().getFirst().getLastName());
                    assertEquals("Email", "edi@gmail.com", response.getData().getLst().getFirst().getEmail());
                    assertEquals("Phone", "08191179657", response.getData().getLst().getFirst().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getLst().getFirst().getId()));
                });
    }

    @Test
    void getContactNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/contacts/hhannagak-nahahab")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isNotFound()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", false, response.isSuccess());
                    assertNotNull(response.getData().getErrors());
                });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("username").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Edi");
        contact.setLastName("Yasa");
        contact.setEmail("edi@gmail.com");
        contact.setPhone("08191179657");
        contact.setUser(user);
        contactRepository.save(contact);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                    assertNull(response.getData().getErrors());
                    assertEquals("FirstName", "Edi", response.getData().getLst().getFirst().getFirstName());
                    assertEquals("LastName", "Yasa", response.getData().getLst().getFirst().getLastName());
                    assertEquals("Email", "edi@gmail.com", response.getData().getLst().getFirst().getEmail());
                    assertEquals("Phone", "08191179657", response.getData().getLst().getFirst().getPhone());
                });
    }

    @Test
    void updateContactBadRequest() throws Exception {
        CreateContactRequest createContactRequest = new CreateContactRequest();
        createContactRequest.setFirstName("");
        createContactRequest.setEmail("salah");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/contacts/hagannaja")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContactRequest))
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isBadRequest()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", false, response.isSuccess());
                    assertNotNull(response.getData().getErrors());
                });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("username").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Edi");
        contact.setLastName("Yasa");
        contact.setEmail("edi@gmail.com");
        contact.setPhone("08191179657");
        contact.setUser(user);
        contactRepository.save(contact);

        UpdateContactRequest updateContactRequest = new UpdateContactRequest();
        updateContactRequest.setFirstName("Edi");
        updateContactRequest.setLastName("Yasa Update");
        updateContactRequest.setEmail("edi@gmail.com");
        updateContactRequest.setPhoneNumber("08191179657");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContactRequest))
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                    assertEquals("FirstName", "Edi", response.getData().getLst().getFirst().getFirstName());
                    assertEquals("LastName", "Yasa Update", response.getData().getLst().getFirst().getLastName());
                    assertEquals("Email", "edi@gmail.com", response.getData().getLst().getFirst().getEmail());
                    assertEquals("Phone", "08191179657", response.getData().getLst().getFirst().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getLst().getFirst().getId()));
                    assertEquals("", "Yasa Update", response.getData().getLst().getFirst().getLastName());
                });
    }

    @Test
    void deleteContactNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contacts/hhannagak-nahahab")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isNotFound()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", false, response.isSuccess());
                    assertNotNull(response.getData().getErrors());
                });
    }

    @Test
    void deleteContactSuccess() throws Exception {
        User user = userRepository.findById("username").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Edi");
        contact.setLastName("Yasa");
        contact.setEmail("edi@gmail.com");
        contact.setPhone("08191179657");
        contact.setUser(user);
        contactRepository.save(contact);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokenTest"))
                .andExpectAll(
                        status().isOk()
                ).andDo(result -> {
                    WebResponse<ResponseData<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals("Is Success Response : ", true, response.isSuccess());
                });
    }

}