package ru.bolshakov.internship.dishes_rating.controller.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
import ru.bolshakov.internship.dishes_rating.dto.ErrorResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.dto.vote.VoteDTO;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.exception.ChangingVoteUnavailable;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.security.JwtTokenService;
import ru.bolshakov.internship.dishes_rating.security.SecurityUser;
import ru.bolshakov.internship.dishes_rating.security.TokenPair;
import ru.bolshakov.internship.dishes_rating.service.VoteService;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

class RestaurantVotingControllerTest extends DishesRatingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    @MockBean
    private JwtTokenService provider;

    private Long restaurantId;
    private Long userId;
    private VoteDTO vote;
    private TokenPair tokenPair;
    private SecurityUser securityUser;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(user(new SecurityUser(1L, "User", "password", Role.USER.name(), true))))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        restaurantId = 50L;
        userId = 1L;
        UserDTO userDTO = new UserDTO(userId, "userName", "User@mail.com", "password", Role.USER.name());
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantId, "restaurant", "description");
        vote = new VoteDTO(1L, userDTO, LocalTime.now(), restaurantDTO);
        tokenPair = new TokenPair("accessToken", "refreshToken");
        securityUser = new SecurityUser(userId, "mail@mail.com", "password", "USER", true);
    }

    @Test
    void vote() throws Exception {
        Mockito.when(voteService.vote(userId, restaurantId)).thenReturn(vote);
        Mockito.when(provider.resolveAccessToken(any())).thenReturn(tokenPair.getAccessToken());
        Mockito.doNothing().when(provider).validateToken(tokenPair.getAccessToken());
        Mockito.when(provider.getAuthentication(tokenPair.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities()));

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/50/vote")
                .param("id", userId.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(vote)));
    }

    @Test
    void voteIfUserHasExpiredToken() throws Exception {
        Mockito.when(voteService.vote(userId, restaurantId)).thenReturn(vote);
        Mockito.when(provider.resolveAccessToken(any())).thenReturn(tokenPair.getAccessToken());
        Mockito.doThrow(AuthorizationFailureException.class).when(provider).validateToken(tokenPair.getAccessToken());
        Mockito.when(provider.getAuthentication(tokenPair.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities()));

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/50/vote")
                .param("id", userId.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void voteIfAlreadyExistsAndChangingUnavailable() throws Exception {
        Mockito.when(voteService.vote(userId, restaurantId)).thenThrow(ChangingVoteUnavailable.class);
        Mockito.when(provider.resolveAccessToken(any())).thenReturn(tokenPair.getAccessToken());
        Mockito.doNothing().when(provider).validateToken(tokenPair.getAccessToken());
        Mockito.when(provider.getAuthentication(tokenPair.getAccessToken()))
                .thenReturn(new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities()));

        mockMvc.perform(MockMvcRequestBuilders.post("/restaurants/50/vote")
                .param("id", userId.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ErrorResponseDTO())));
    }
}
