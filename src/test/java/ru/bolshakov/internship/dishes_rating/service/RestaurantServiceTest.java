package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.search.SearchRequest;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;
import ru.bolshakov.internship.dishes_rating.model.User;
import ru.bolshakov.internship.dishes_rating.model.Vote;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVoteRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.RestaurantMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    private static final Restaurant NEW_RESTAURANT = new Restaurant("newName1", "newDescription1");
    private static final Restaurant EXISTENT_RESTAURANT = new Restaurant(1L, NEW_RESTAURANT.getRestaurantName(), NEW_RESTAURANT.getDescription());
    private static final RestaurantSavingRequestDTO SAVING_REQUEST_DTO = new RestaurantSavingRequestDTO(NEW_RESTAURANT.getRestaurantName(), NEW_RESTAURANT.getDescription());
    private static final Long RESTAURANT_ID = 3L;
    private static final Long RATING = 100L;

    private static final List<Restaurant> EXISTENT_RESTAURANTS;
    private static final List<User> EXISTENT_USERS;

    static {
        EXISTENT_RESTAURANTS = Arrays.asList(
                new Restaurant(123L, "restaurant1", "description1"),
                new Restaurant(124L, "restaurant2", "description2"),
                new Restaurant(125L, "restaurant3", "description3"),
                new Restaurant(126L, "restaurant4", "description4"));

        EXISTENT_USERS = Arrays.asList(
                new User(53L, "userName1", "user1@mail.com", "password", Role.USER),
                new User(54L, "userName2", "user2@mail.com", "password", Role.USER),
                new User(55L, "userName3", "user3@mail.com", "password", Role.USER),
                new User(56L, "userName4", "user4@mail.com", "password", Role.USER),
                new User(57L, "userName5", "user5@mail.com", "password", Role.USER),
                new User(58L, "userName6", "user6@mail.com", "password", Role.USER));
    }

    @Mock
    private JpaRestaurantRepository restaurantRepository;

    @Mock
    private JpaVoteRepository voteRepository;

    @Spy
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService service;

    @Test
    void create() {
        Mockito.when(restaurantRepository.save(NEW_RESTAURANT))
                .thenReturn(EXISTENT_RESTAURANT);

        RestaurantDTO createdRestaurantDTO = service.create(SAVING_REQUEST_DTO);
        assertNotNull(createdRestaurantDTO.getId());
        assertEquals(NEW_RESTAURANT.getRestaurantName(), createdRestaurantDTO.getRestaurantName());
        assertEquals(NEW_RESTAURANT.getDescription(), createdRestaurantDTO.getDescription());
        assertNull(createdRestaurantDTO.getRating());
    }

    @Test
    void createIfAlreadyExists() {
        Mockito.when(restaurantRepository.save(NEW_RESTAURANT))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> service.create(SAVING_REQUEST_DTO));
    }

    @Test
    void update() {
        Mockito.when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId()))
                .thenReturn(Optional.of(EXISTENT_RESTAURANT));
        Mockito.when(restaurantRepository.findByRestaurantName(EXISTENT_RESTAURANT.getRestaurantName()))
                .thenReturn(Optional.empty());
        Mockito.when(restaurantRepository.save(EXISTENT_RESTAURANT)).thenReturn(EXISTENT_RESTAURANT);

        RestaurantDTO updatedRestaurant = service.update(SAVING_REQUEST_DTO, EXISTENT_RESTAURANT.getId());

        assertNotNull(updatedRestaurant.getId());
        assertEquals(EXISTENT_RESTAURANT.getRestaurantName(), updatedRestaurant.getRestaurantName());
        assertEquals(EXISTENT_RESTAURANT.getDescription(), updatedRestaurant.getDescription());
        assertNull(updatedRestaurant.getRating());
    }

    @Test
    void updateIfRestaurantDoesNotExist() {
        Mockito.when(restaurantRepository.findById(NEW_RESTAURANT.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(SAVING_REQUEST_DTO, NEW_RESTAURANT.getId()));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(restaurantRepository).deleteById(RESTAURANT_ID);
        Mockito.when(restaurantRepository.findById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.delete(RESTAURANT_ID));
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_ID));
    }

    @Test
    void deleteIfRestaurantDoesNotExist() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(restaurantRepository).deleteById(RESTAURANT_ID);

        assertThrows(NotFoundException.class, () -> service.delete(RESTAURANT_ID));
    }

    @Test
    void getRestaurantWithRatingByDate() {
        Mockito.when(restaurantRepository.findById(NEW_RESTAURANT.getId())).thenReturn(Optional.of(NEW_RESTAURANT));
        Mockito.when(voteRepository.countByVotingDateTimeBetweenAndRestaurant_Id(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX), NEW_RESTAURANT.getId())).thenReturn(Optional.of(RATING));

        RestaurantDTO returnedRestaurant = service.getRestaurantWithRatingByDate(NEW_RESTAURANT.getId(), LocalDate.now());
        assertEquals(NEW_RESTAURANT.getId(), returnedRestaurant.getId());
        assertEquals(NEW_RESTAURANT.getRestaurantName(), returnedRestaurant.getRestaurantName());
        assertEquals(NEW_RESTAURANT.getDescription(), returnedRestaurant.getDescription());
        assertEquals(RATING, returnedRestaurant.getRating());
    }

    @Test
    void get() {
        Mockito.when(restaurantRepository.findById(NEW_RESTAURANT.getId())).thenReturn(Optional.of(NEW_RESTAURANT));

        RestaurantDTO returnedRestaurant = service.get(NEW_RESTAURANT.getId());
        assertEquals(NEW_RESTAURANT.getId(), returnedRestaurant.getId());
        assertEquals(NEW_RESTAURANT.getRestaurantName(), returnedRestaurant.getRestaurantName());
        assertEquals(NEW_RESTAURANT.getDescription(), returnedRestaurant.getDescription());
        assertNull(returnedRestaurant.getRating());
    }

    @Test
    void getAllWithRatingByDate() {
        List<Vote> votes = Arrays.asList(
                new Vote(1L, EXISTENT_USERS.get(0), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(0)),
                new Vote(2L, EXISTENT_USERS.get(1), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(0)),
                new Vote(3L, EXISTENT_USERS.get(2), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(0)),
                new Vote(4L, EXISTENT_USERS.get(3), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(1)),
                new Vote(5L, EXISTENT_USERS.get(4), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(1)),
                new Vote(6L, EXISTENT_USERS.get(5), LocalDateTime.now(), EXISTENT_RESTAURANTS.get(2)));

        PageRequest pageRequest = PageRequest.of(0, 3);
        Mockito.when(restaurantRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(EXISTENT_RESTAURANTS));
        Mockito.when(voteRepository.findAllByVotingDateTimeBetween(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)))
                .thenReturn(votes);

        List<RestaurantDTO> restaurantsWithRating = service.getAllWithRatingByDate(
                LocalDate.now(), pageRequest,
                new SearchRequest()
        );

        for(int i = 0; i < (restaurantsWithRating.size() - 1); i++) {
            Long restaurantId = EXISTENT_RESTAURANTS.get(i).getId();
            assertEquals(restaurantId, restaurantsWithRating.get(i).getId());
            assertEquals(votes.stream().filter(vote -> vote.getRestaurant().getId().equals(restaurantId)).count(), restaurantsWithRating.get(i).getRating());
        }

        assertEquals(EXISTENT_RESTAURANTS.get(3).getId(), restaurantsWithRating.get(3).getId());
        assertNull(restaurantsWithRating.get(3).getRating());
    }

    @Test
    void getAll() {
        Mockito.when(restaurantRepository.findAll()).thenReturn(EXISTENT_RESTAURANTS);

        List<RestaurantDTO> restaurantsWithRating = service.getAll();

        for(int i = 0; i < restaurantsWithRating.size(); i++) {
            assertEquals(EXISTENT_RESTAURANTS.get(i).getId(), restaurantsWithRating.get(i).getId());
            assertNull(restaurantsWithRating.get(i).getRating());
        }
    }
}
