package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.bolshakov.internship.dishes_rating.dto.AuthResponseDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.search.RestaurantSearchRequest;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.AuthorizationFailureException;
import ru.bolshakov.internship.dishes_rating.exception.ChangingVoteUnavailable;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.model.jpa.VerificationToken;
import ru.bolshakov.internship.dishes_rating.properties.VotingProperties;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVerificationTokenRepository;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    UserAccountVerificationService verificationService;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    @Autowired
    JpaVerificationTokenRepository verificationTokenRepository;

    @MockBean
    MailMessageService messageService;

    @MockBean
    VotingProperties votingProperties;

    @Autowired
    JpaRestaurantRepository restaurantRepository;

    private User user1;
    private User user2;
    private User user3;
    private RestaurantDTO restaurant1;
    private RestaurantDTO restaurant2;
    private RestaurantDTO restaurant3;

    @BeforeAll
    void setUp() {
        user1 = userRepository.save(new User("user1", "user1@mail.ru", "password123"));
        user2 = userRepository.save(new User("user2", "user2@mail.ru", "password123"));
        user3 = userRepository.save(new User("user3", "user3@mail.ru", "password123"));
        restaurant1 = restaurantService.create(new RestaurantSavingRequestDTO("Restaurant1", "description1"));
        restaurant2 = restaurantService.create(new RestaurantSavingRequestDTO("Restaurant2", "description2"));
        restaurant3 = restaurantService.create(new RestaurantSavingRequestDTO("Restaurant3", "description3"));
    }

    @Test
    void userFullRegistrationAndAuthorization() {
        Mockito.doNothing().when(messageService).sendLetterAsynchronously(Mockito.any(), Mockito.any(), Mockito.any());

        UserSavingRequestDTO userSavingRequestDTO = new UserSavingRequestDTO("Nikolay", "bolshakov_ns99@mail.ru", "password123");

        UserDTO userDTO = userService.create(userSavingRequestDTO);

        assertThrows(AuthorizationFailureException.class,
                () -> authorizationService.authorize(new AuthorizationRequestDTO(userSavingRequestDTO.getEmail(), userSavingRequestDTO.getPassword()))
        );

        VerificationToken tokenById = verificationTokenRepository.findAll().stream().filter(token -> token.getUser().getId().equals(userDTO.getId())).findFirst().orElseThrow();
        verificationService.activateUserAccount(tokenById.getUuid());

        assertDoesNotThrow(() -> authorizationService.authorize(new AuthorizationRequestDTO(userSavingRequestDTO.getEmail(), userSavingRequestDTO.getPassword())));
        AuthResponseDTO authResponseDTO = authorizationService.authorize(new AuthorizationRequestDTO(userSavingRequestDTO.getEmail(), userSavingRequestDTO.getPassword()));
        assertNotNull(authResponseDTO.getAccessToken());
        assertNotNull(authResponseDTO.getRefreshToken());
    }

    @Test
    void restaurantAndMenuManagement() {
        assertEquals(this.restaurant1.getRestaurantName(), restaurant1.getRestaurantName());

        RestaurantSavingRequestDTO restaurantToUpdate = new RestaurantSavingRequestDTO("New Name", "New Description");
        RestaurantDTO updatedRestaurantDTO = restaurantService.update(restaurantToUpdate, restaurant1.getId());
        Long restaurantId = updatedRestaurantDTO.getId();

        assertEquals(restaurantToUpdate.getRestaurantName(), updatedRestaurantDTO.getRestaurantName());

        DishSavingRequestDTO newDish1 = new DishSavingRequestDTO("Dish Name1", 10050L);
        DishSavingRequestDTO newDish2 = new DishSavingRequestDTO("Dish Name2", 15000L);

        DishDTO dishDTO2 = menuService.addDish(newDish2, restaurantId);
        DishDTO dishDTO1 = menuService.addDish(newDish1, restaurantId);

        MenuDTO menuByRestaurantId = menuService.getMenuByRestaurantId(restaurantId);
        List<DishDTO> dishes = menuByRestaurantId.getDishes();

        assertEquals(2, dishes.size());
        assertEquals(dishDTO1.getName(), dishes.get(0).getName());
        assertEquals(dishDTO1.getFormattedPrice(), dishes.get(0).getFormattedPrice());
        assertEquals(dishDTO2.getName(), dishes.get(1).getName());
        assertEquals(dishDTO2.getFormattedPrice(), dishes.get(1).getFormattedPrice());

        assertDoesNotThrow(() -> menuService.deleteDish(restaurantId, dishDTO1.getId()));
        assertThrows(NotFoundException.class, () -> menuService.getDish(restaurantId, dishDTO1.getId()));
        assertEquals(1, menuService.getMenuByRestaurantId(restaurantId).getDishes().size());

        assertDoesNotThrow(() -> restaurantService.delete(restaurantId));
        assertThrows(NotFoundException.class, () -> restaurantService.get(restaurantId));
    }

    @Test
    void usersVoteForRestaurantsAndChangingVoteIsAvailable() {
        Mockito.when(votingProperties.getBoundaryTime()).thenReturn(LocalTime.MAX);

        List<RestaurantDTO> allWithRatingBeforeVoting = restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new RestaurantSearchRequest());

        assertEquals(3, allWithRatingBeforeVoting.size());
        allWithRatingBeforeVoting.forEach(restaurantDTO -> assertNull(restaurantDTO.getRating()));

        assertAll("Users vote",
                () -> Assertions.assertDoesNotThrow(() -> voteService.vote(user1.getId(), restaurant1.getId())),
                () -> Assertions.assertDoesNotThrow(() -> voteService.vote(user2.getId(), restaurant1.getId())),
                () -> Assertions.assertDoesNotThrow(() -> voteService.vote(user3.getId(), restaurant2.getId()))
        );

        List<RestaurantDTO> allWithRatingAfterVoting = restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new RestaurantSearchRequest());

        assertEquals(2L, allWithRatingAfterVoting.get(0).getRating());
        assertEquals(1L, allWithRatingAfterVoting.get(1).getRating());
        assertNull(allWithRatingAfterVoting.get(2).getRating());

        assertDoesNotThrow(() -> voteService.vote(user1.getId(), restaurant3.getId()));

        List<RestaurantDTO> allWithRatingAfterChangingVote = restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new RestaurantSearchRequest());

        assertEquals(1L, allWithRatingAfterChangingVote.get(0).getRating());
        assertEquals(1L, allWithRatingAfterChangingVote.get(1).getRating());
        assertEquals(1L, allWithRatingAfterChangingVote.get(2).getRating());
    }

    @Test
    void usersVoteForRestaurantsAndChangingVoteIsUnavailable() {
        Mockito.when(votingProperties.getBoundaryTime()).thenReturn(LocalTime.MIN);

        List<RestaurantDTO> allWithRatingBeforeVoting = restaurantService.getAllWithRatingByDate(PageRequest.of(0, 3),
                new RestaurantSearchRequest());

        assertEquals(3, allWithRatingBeforeVoting.size());
        allWithRatingBeforeVoting.forEach(restaurantDTO -> assertNull(restaurantDTO.getRating()));

        assertThrows(ChangingVoteUnavailable.class, () -> voteService.vote(user1.getId(), restaurant1.getId()));
        assertThrows(ChangingVoteUnavailable.class, () -> voteService.vote(user2.getId(), restaurant1.getId()));
        assertThrows(ChangingVoteUnavailable.class, () -> voteService.vote(user3.getId(), restaurant2.getId()));
    }
}