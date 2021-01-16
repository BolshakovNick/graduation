package ru.bolshakov.internship.dishes_rating.controller.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.bolshakov.internship.dishes_rating.DishesRatingApplicationTests;
import ru.bolshakov.internship.dishes_rating.dto.ErrorResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.search.SearchRequest;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.service.RestaurantService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class RestaurantViewingControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(user(new SecurityUser(1L, "User", "password", Role.USER.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void get() throws Exception {
        Long restaurantId = 1L;
        RestaurantDTO dto = new RestaurantDTO(1L, "name", "description");
        Mockito.when(restaurantService.getRestaurantWithRatingByDate(restaurantId)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void getAll() throws Exception {
        List<RestaurantDTO> restaurants = Arrays.asList(
                new RestaurantDTO(1L, "name1", "description1", 100L),
                new RestaurantDTO(2L, "name2", "description2", 200L),
                new RestaurantDTO(3L, "name3", "description3", 300L));

        Mockito.when(restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new SearchRequest()))
                .thenReturn(restaurants);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(restaurants)));
    }

    @Test
    void getAllRestaurantsIfPageSizeIsGreaterThanMax() throws Exception {
        List<RestaurantDTO> restaurants = Arrays.asList(
                new RestaurantDTO(1L, "name1", "description1", 100L),
                new RestaurantDTO(2L, "name2", "description2", 200L),
                new RestaurantDTO(3L, "name3", "description3", 300L));

        Mockito.when(restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new SearchRequest()))
                .thenReturn(restaurants);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants")
                .param("size", "15"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(restaurants)));
    }

    @Test
    void getIfNotExist() throws Exception {
        Mockito.when(restaurantService.getRestaurantWithRatingByDate(1L)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }
}
