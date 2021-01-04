package ru.bolshakov.internship.dishes_rating.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.bolshakov.internship.dishes_rating.DishesRatingApplicationTests;
import ru.bolshakov.internship.dishes_rating.dto.search.UserSearchRequest;
import ru.bolshakov.internship.dishes_rating.dto.user.UpdatingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ContextConfiguration
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagementControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    private Long userId;
    private UserDTO userDTO;
    private UpdatingRequestDTO requestDTO;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/profile")
                        .with(user(new SecurityUser(1L, "admin@mail.com", "password", Role.ADMIN.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        userId = 123L;
        userDTO = new UserDTO(userId, "Some Name", "mail@mail.com", "password", "USER");
        requestDTO = new UpdatingRequestDTO(userDTO.getUserName(), userDTO.getEmail(), userDTO.getPassword(), Role.valueOf(userDTO.getRole()));
    }

    @Test
    void getAllUsers() throws Exception {
        UserDTO userDTO1 = new UserDTO(120L, "Some Name1", "mail@mail.com", "password", "ADMIN");
        UserDTO userDTO2 = new UserDTO(121L, "Some Name2", "mail2@mail.com", "password", "USER");
        UserDTO userDTO3 = new UserDTO(122L, "Some Name3", "mail3@mail.com", "password", "USER");

        List<UserDTO> users = Arrays.asList(userDTO1, userDTO2, userDTO3);

        Mockito.when(service.getAll(PageRequest.of(0, 3), new UserSearchRequest())).thenReturn(users);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(users)));
    }

    @Test
    void getAllUsersIfPageSizeIsGreaterThanMax() throws Exception {
        UserDTO userDTO1 = new UserDTO(120L, "Some Name1", "mail@mail.com", "password", "ADMIN");
        UserDTO userDTO2 = new UserDTO(121L, "Some Name2", "mail2@mail.com", "password", "USER");
        UserDTO userDTO3 = new UserDTO(122L, "Some Name3", "mail3@mail.com", "password", "USER");

        List<UserDTO> users = Arrays.asList(userDTO1, userDTO2, userDTO3);

        Mockito.when(service.getAll(PageRequest.of(0, 3), new UserSearchRequest())).thenReturn(users);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .param("size", "15"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(users)));
    }

    @Test
    void getUserById() throws Exception {
        Mockito.when(service.get(userId)).thenReturn(userDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    void getIfUserDoesNotExist() throws Exception {
        Mockito.when(service.get(userId)).thenThrow(NotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        Mockito.when(service.update(userId, requestDTO))
                .thenReturn(userDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId).contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(userId.toString()));
    }

    @Test
    void updateIfUserDoesNotExist() throws Exception {
        Mockito.when(service.update(userId, requestDTO))
                .thenThrow(NotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId).contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}