package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.junit.jupiter.api.Test;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.dto.vote.VoteDTO;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.model.jpa.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoteMapperTest {

    private final VoteMapper mapper = new VoteMapper();

    @Test
    void toEntity() {
        UserDTO userDTO = new UserDTO(1L, "userName", "User@mail.com", "password", Role.USER.name());
        RestaurantDTO restaurantDTO = new RestaurantDTO(50L, "restaurant", "description");
        VoteDTO dto = new VoteDTO(userDTO, LocalTime.of(10, 59), restaurantDTO);

        Vote vote = mapper.toEntity(dto);

        assertEquals(dto.getId(), vote.getId());
        assertEquals(dto.getUser().getId(), vote.getUser().getId());
        assertEquals(LocalDate.now(), vote.getVotingDateTime().toLocalDate());
        assertEquals(dto.getLocalTime(), vote.getVotingDateTime().toLocalTime());
        assertEquals(dto.getRestaurant().getId(), vote.getRestaurant().getId());
    }

    @Test
    void toDTO() {
        Restaurant restaurant = new Restaurant(123L, "restaurant1", "description1");
        User user = new User(53L, "userName", "user@mail.com", "password", Role.USER);
        Vote vote = new Vote(user, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 59)), restaurant);

        VoteDTO dto = mapper.toDTO(vote);

        assertEquals(vote.getId(), dto.getId());
        assertEquals(vote.getUser().getId(), dto.getUser().getId());
        assertEquals(vote.getVotingDateTime().toLocalTime(), dto.getLocalTime());
        assertEquals(vote.getRestaurant().getId(), dto.getRestaurant().getId());
    }

    @Test
    void toDTOs() {
        RestaurantDTO restaurantDTO1 = new RestaurantDTO(123L, "restaurant1", "description1");
        RestaurantDTO restaurantDTO2 = new RestaurantDTO(124L, "restaurant2", "description2");
        RestaurantDTO restaurantDTO3 = new RestaurantDTO(125L, "restaurant3", "description3");

        UserDTO userDTO1 = new UserDTO(53L, "userName1", "user1@mail.com", "password", Role.USER.name());
        UserDTO userDTO2 = new UserDTO(54L, "userName2", "user2@mail.com", "password", Role.USER.name());
        UserDTO userDTO3 = new UserDTO(55L, "userName3", "user3@mail.com", "password", Role.USER.name());

        List<VoteDTO> expectedVotes = Arrays.asList(
                new VoteDTO(userDTO1, LocalTime.of(10, 59), restaurantDTO1),
                new VoteDTO(userDTO2, LocalTime.of(12, 30), restaurantDTO2),
                new VoteDTO(userDTO3, LocalTime.of(14, 1), restaurantDTO3));

        Restaurant restaurant1 = new Restaurant(123L, "restaurant1", "description1");
        Restaurant restaurant2 = new Restaurant(124L, "restaurant2", "description2");
        Restaurant restaurant3 = new Restaurant(125L, "restaurant3", "description3");

        User user1 = new User(53L, "userName1", "user1@mail.com", "password", Role.USER);
        User user2 = new User(54L, "userName2", "user2@mail.com", "password", Role.USER);
        User user3 = new User(55L, "userName3", "user3@mail.com", "password", Role.USER);

        List<Vote> votes = Arrays.asList(
                new Vote(user1, LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 59)), restaurant1),
                new Vote(user2, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30)), restaurant2),
                new Vote(user3, LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 1)), restaurant3));

        List<VoteDTO> returnedDTO = mapper.toDTOs(votes);

        assertEquals(expectedVotes.get(0).getId(), returnedDTO.get(0).getId());
        assertEquals(expectedVotes.get(0).getUser().getId(), returnedDTO.get(0).getUser().getId());
        assertEquals(expectedVotes.get(0).getLocalTime(), returnedDTO.get(0).getLocalTime());
        assertEquals(expectedVotes.get(0).getRestaurant().getId(), returnedDTO.get(0).getRestaurant().getId());

        assertEquals(expectedVotes.get(1).getId(), returnedDTO.get(1).getId());
        assertEquals(expectedVotes.get(1).getUser().getId(), returnedDTO.get(1).getUser().getId());
        assertEquals(expectedVotes.get(1).getLocalTime(), returnedDTO.get(1).getLocalTime());
        assertEquals(expectedVotes.get(1).getRestaurant().getId(), returnedDTO.get(1).getRestaurant().getId());

        assertEquals(expectedVotes.get(2).getId(), returnedDTO.get(2).getId());
        assertEquals(expectedVotes.get(2).getUser().getId(), returnedDTO.get(2).getUser().getId());
        assertEquals(expectedVotes.get(2).getLocalTime(), returnedDTO.get(2).getLocalTime());
        assertEquals(expectedVotes.get(2).getRestaurant().getId(), returnedDTO.get(2).getRestaurant().getId());
    }
}