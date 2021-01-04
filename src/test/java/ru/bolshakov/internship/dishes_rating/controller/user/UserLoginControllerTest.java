package ru.bolshakov.internship.dishes_rating.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.bolshakov.internship.dishes_rating.DishesRatingApplicationTests;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.ErrorResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.FieldErrorDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.exception.NonUniqueParamException;
import ru.bolshakov.internship.dishes_rating.service.AuthorizationService;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import java.util.Collections;

class  UserLoginControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private UserService service;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void register() throws Exception {
        UserSavingRequestDTO newUser = new UserSavingRequestDTO("first", "mail@gmail.com", "password123");
        UserDTO createdUser = new UserDTO(1L, "first", "mail@gmail.com", "password123", "USER");

        Mockito.when(service.isUserExists(newUser))
                .thenReturn(false);

        Mockito.when(service.create(newUser))
                .thenReturn(createdUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(newUser)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

    @Test
    void registerIfEmailAlreadyExists() throws Exception {
        UserSavingRequestDTO userWithExistentMail = new UserSavingRequestDTO("first", "ma@flool.com", "password123");

        Mockito.when(service.create(userWithExistentMail))
                .thenThrow(new NonUniqueParamException("Email must be unique"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(userWithExistentMail)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO("Email must be unique"))));
    }

    @Test
    void registerIfNameIsTooShort() throws Exception {
        UserSavingRequestDTO userWithInvalidName = new UserSavingRequestDTO("fir", "mairsdsfrrl@gmail.com", "password123");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        new ErrorResponseDTO(Collections.singletonList(new FieldErrorDTO("userName", "size must be between 5 and 50"))))));
    }

    @Test
    void registerIfNameIsTooLong() throws Exception {
        UserSavingRequestDTO userWithInvalidName = new UserSavingRequestDTO("firstfirstfirstfirstfirstfirstfirstfirstfirstssssss", "mail@gmail.com", "password123");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        new ErrorResponseDTO(Collections.singletonList(new FieldErrorDTO("userName", "size must be between 5 and 50"))))));
    }

    @Test
    void registerIfEmailIsInvalid() throws Exception {
        UserSavingRequestDTO userWithInvalidEmail = new UserSavingRequestDTO("first", "someIncorrectMail", "password123");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(userWithInvalidEmail)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        (new ErrorResponseDTO(Collections.singletonList(new FieldErrorDTO("email", "must be a well-formed email address")))))));
    }

    @Test
    void registerIfPasswordIsInvalid() throws Exception {
        UserSavingRequestDTO userWithInvalidPassword = new UserSavingRequestDTO("first", "mail@gmail.com", "passwor");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-up").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(userWithInvalidPassword)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(
                        new ErrorResponseDTO(Collections.singletonList(new FieldErrorDTO("password", "size must be between 8 and 50"))))));
    }

    @Test
    void authorize() throws Exception {
        AuthorizationRequestDTO requestDTO = new AuthorizationRequestDTO("mail@gmail.com", "password123");

        AuthResponseDTO response = new AuthResponseDTO("accessToken", "refreshToken");
        Mockito.when(authorizationService.authorize(requestDTO)).thenReturn(response);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-in").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    void authorizeIfUserDoesNotExist() throws Exception {
        AuthorizationRequestDTO requestDTO = new AuthorizationRequestDTO("mail@gmail.com", "password123");

        Mockito.when(authorizationService.authorize(requestDTO)).thenThrow(AuthorizationFailureException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-in").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void authorizeIfPasswordIncorrect() throws Exception {
        AuthorizationRequestDTO requestDTO = new AuthorizationRequestDTO("mail@gmail.com", "password123");

        Mockito.when(authorizationService.authorize(requestDTO)).thenThrow(AuthorizationFailureException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/sign-in").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}