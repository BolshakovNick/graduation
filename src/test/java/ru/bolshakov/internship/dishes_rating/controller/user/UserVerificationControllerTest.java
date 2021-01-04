package ru.bolshakov.internship.dishes_rating.controller.user;

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
import ru.bolshakov.internship.dishes_rating.service.UserAccountVerificationService;

import java.util.UUID;

class UserVerificationControllerTest extends DishesRatingApplicationTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserAccountVerificationService service;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void confirm() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Mockito.doNothing().when(service).activateUserAccount(uuid);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorization/registration-confirm")
                .param("uuid", uuid))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}