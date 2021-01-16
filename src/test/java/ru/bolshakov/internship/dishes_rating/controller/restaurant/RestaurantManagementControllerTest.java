package ru.bolshakov.internship.dishes_rating.controller.restaurant;

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
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.NonUniqueParamException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.service.RestaurantService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class RestaurantManagementControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    private RestaurantDTO dto;
    private RestaurantSavingRequestDTO requestDTO;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(user(new SecurityUser(1L, "Admin", "password", Role.ADMIN.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        dto = new RestaurantDTO(1L, "Rest Name", "description");
        requestDTO = new RestaurantSavingRequestDTO(dto.getRestaurantName(), dto.getDescription());
    }

    @Test
    void create() throws Exception {
        Mockito.when(restaurantService.isRestaurantNameAlreadyExists(requestDTO)).thenReturn(false);
        Mockito.when(restaurantService.create(requestDTO)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(dto.getId().toString()));
    }

    @Test
    void update() throws Exception {
        Mockito.when(restaurantService.update(requestDTO, dto.getId())).thenReturn(dto);
        Mockito.when(restaurantService.isRestaurantNameAlreadyExists(requestDTO)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(dto.getId().toString()));
    }

    @Test
    void updateIfRestaurantNameAlreadyExists() throws Exception {
        Mockito.when(restaurantService.update(requestDTO, dto.getId())).thenThrow(new NonUniqueParamException("name must be unique"));

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
