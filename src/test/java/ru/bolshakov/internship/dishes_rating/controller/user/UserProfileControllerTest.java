package ru.bolshakov.internship.dishes_rating.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.bolshakov.internship.dishes_rating.DishesRatingApplicationTests;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class UserProfileControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @MockBean
    private JwtTokenService tokenService;

    private Long userId;
    private UserDTO userDTO;
    private TokenPair tokens;
    private SecurityUser securityUser;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/profile")
                        .with(user(new SecurityUser(1L, "user@mail.com", "password", Role.USER.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        userId = 123L;
        userDTO = new UserDTO(userId, "Some Name", "mail@mail.com", "password", "USER");
        tokens = new TokenPair("accessToken", "refreshToken");
        securityUser = new SecurityUser(userId, userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole(), true);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(tokenService.resolveAccessToken(any())).thenReturn(tokens.getAccessToken());
        Mockito.doNothing().when(tokenService).validateToken(tokens.getAccessToken());
        Mockito.when(tokenService.getAuthentication(tokens.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities()));
        Mockito.when(service.get(userId)).thenReturn(userDTO);
    }

    @Test
    void getUserById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    void getIfUserDoesNotExist() throws Exception {
        Mockito.when(service.get(userId)).thenThrow(NotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        UserSavingRequestDTO requestDTO = new UserSavingRequestDTO(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword());

        Mockito.when(service.update(userId, requestDTO))
                .thenReturn(userDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/profile").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(userId.toString()));
    }

    @Test
    void updateIfUserDoesNotExist() throws Exception {
        UserSavingRequestDTO requestDTO = new UserSavingRequestDTO(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword());

        Mockito.when(service.update(userId, requestDTO))
                .thenThrow(NotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/profile").contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doNothing().when(service).delete(userId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/profile"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}