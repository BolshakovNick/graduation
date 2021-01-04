package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.springframework.stereotype.Component;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VoteMapper {

    public Vote toEntity(VoteDTO dto) {
        User user = new User(dto.getUser().getId(), dto.getUser().getUserName(), dto.getUser().getEmail(), dto.getUser().getPassword(), Role.valueOf(dto.getUser().getRole()));
        Restaurant restaurant = new Restaurant(dto.getRestaurant().getId(), dto.getRestaurant().getRestaurantName(), dto.getRestaurant().getDescription());
        return new Vote(dto.getId(), user, LocalDateTime.of(LocalDate.now(), dto.getLocalTime()), restaurant);
    }

    public VoteDTO toDTO(Vote vote) {
        User user = vote.getUser();
        Restaurant restaurant = vote.getRestaurant();
        return new VoteDTO(vote.getId(),
                new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole().name()),
                vote.getVotingDateTime().toLocalTime(),
                new RestaurantDTO(restaurant.getId(), restaurant.getRestaurantName(), restaurant.getDescription()));
    }

    public VoteDTO toDTO(UserDTO userDTO, RestaurantDTO restaurantDTO, LocalTime time) {
        return toDTO(userDTO, restaurantDTO, null, time);
    }

    public VoteDTO toDTO(UserDTO userDTO, RestaurantDTO restaurantDTO, Long voteId, LocalTime time) {
        return new VoteDTO(voteId, userDTO, time, restaurantDTO);
    }

    public List<VoteDTO> toDTOs(List<Vote> dishes) {
        return dishes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
