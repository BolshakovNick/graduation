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
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.service.MenuService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class MenuManagementControllerTest extends DishesRatingApplicationTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    private Long restaurantId;
    private Long dishId;
    private DishSavingRequestDTO requestDTO;
    private DishDTO dto;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(user(new SecurityUser(1L, "Admin", "password", Role.ADMIN.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        restaurantId = 20L;
        dishId = 540L;
        requestDTO = new DishSavingRequestDTO("name", 10005L);
        dto = new DishDTO(dishId, requestDTO.getName(), requestDTO.getPrice(), 25L, "100.05");
    }

    @Test
    void create() throws Exception {
        Mockito.when(menuService.addDish(requestDTO, restaurantId)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/" + restaurantId + "/menu/dishes/")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(dto.getId().toString()));
    }

    @Test
    void update() throws Exception {
        Mockito.when(menuService.updateDish(requestDTO, restaurantId, dishId)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/" + restaurantId + "/menu/dishes/" + dishId)
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(dto.getId().toString()));
    }

    @Test
    void updateDishIfDoesNotExist() throws Exception {
        Mockito.when(menuService.updateDish(requestDTO, restaurantId, dishId)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/" + restaurantId + "/menu/dishes/" + dishId)
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }

    @Test
    void updateDishIfDoesNotExistInMenu() throws Exception {
        Mockito.when(menuService.updateDish(requestDTO, restaurantId, dishId)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/" + restaurantId + "/menu/dishes/" + dishId)
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/" + restaurantId + "/menu/dishes/" + dishId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}