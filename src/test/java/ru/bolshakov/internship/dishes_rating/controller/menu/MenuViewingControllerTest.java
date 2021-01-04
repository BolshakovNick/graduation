package ru.bolshakov.internship.dishes_rating.controller.menu;

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
import ru.bolshakov.internship.dishes_rating.dto.ErrorResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.service.MenuService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class MenuViewingControllerTest extends DishesRatingApplicationTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

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
    void getDish() throws Exception {
        Long restaurantId = 1L;
        DishDTO dishDTO = new DishDTO(23L, "dish", 10005L, 50L, "100.05");
        Mockito.when(menuService.getDish(restaurantId, dishDTO.getId())).thenReturn(dishDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/menu/dishes/" + dishDTO.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(dishDTO)));
    }

    @Test
    void getDishIfDoesNotExist() throws Exception {
        Long restaurantId = 1L;
        DishDTO dishDTO = new DishDTO(23L, "dish", 10005L, 50L, "100.05");
        Mockito.when(menuService.getDish(restaurantId, dishDTO.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/menu/dishes/" + dishDTO.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }

    @Test
    void getDishIfDoesNotExistInMenu() throws Exception {
        Long restaurantId = 1L;
        DishDTO dishDTO = new DishDTO(23L, "dish", 10005L, 50L, "100.05");
        Mockito.when(menuService.getDish(restaurantId, dishDTO.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/1/menu/dishes/" + dishDTO.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }

    @Test
    void getMenu() throws Exception {
        Long restaurantId = 15L;
        List<DishDTO> dishes = Arrays.asList(
                new DishDTO(23L, "dish", 10005L, 50L, "100.05"),
                new DishDTO(24L, "dish", 23752L, 51L, "237.52"),
                new DishDTO(25L, "dish", 543235L, 52L, "5432.35"));

        MenuDTO menuDTO = new MenuDTO(LocalDate.now(), restaurantId, 192330L, "1923.30", dishes);

        Mockito.when(menuService.getMenuByRestaurantId(restaurantId))
                .thenReturn(menuDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/" + restaurantId + "/menu"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(menuDTO)));
    }
}