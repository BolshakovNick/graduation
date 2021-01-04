package ru.bolshakov.internship.dishes_rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantDTO;
import ru.bolshakov.internship.dishes_rating.dto.restaurant.RestaurantSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.search.RestaurantSearchRequest;
import ru.bolshakov.internship.dishes_rating.exception.NonUniqueParamException;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaVoteRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.RestaurantMapper;
import ru.bolshakov.internship.dishes_rating.service.specification.builder.RestaurantSpecificationBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    protected final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final JpaRestaurantRepository restaurantRepository;

    private final JpaVoteRepository voteRepository;

    private final RestaurantMapper mapper;

    public RestaurantService(JpaRestaurantRepository restaurantRepository,
                             JpaVoteRepository voteRepository,
                             RestaurantMapper mapper) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.mapper = mapper;
    }

    @Transactional
    public RestaurantDTO create(RestaurantSavingRequestDTO dto) {
        Restaurant createdRestaurant = restaurantRepository.save(mapper.toEntity(dto));
        return mapper.toDTO(createdRestaurant);
    }

    @Transactional
    public RestaurantDTO update(RestaurantSavingRequestDTO dto, Long restaurantId) {
        if (isRestaurantNameAlreadyExists(dto)) {
            throw new NonUniqueParamException("Restaurant with such name already exists");
        }
        Restaurant entity = mapper.toEntity(getRestaurantEntity(restaurantId), dto);
        return mapper.toDTO(restaurantRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        try {
            restaurantRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Deleting {} failed. Possible reason: restaurant with id {} does not exist in database", id, id);
            throw new NotFoundException("Restaurant with such ID not found");
        }
    }

    public RestaurantDTO getRestaurantWithRatingByDate(Long id) {
        return getRestaurantWithRatingByDate(id, LocalDate.now());
    }

    public RestaurantDTO getRestaurantWithRatingByDate(Long id, LocalDate date) {
        Restaurant returnedRestaurant = getRestaurantEntity(id);

        return mapper.toDTO(
                returnedRestaurant,
                voteRepository.countByVotingDateTimeBetweenAndRestaurant_Id(date.atStartOfDay(), date.atTime(LocalTime.MAX), id).orElse(null));
    }

    public RestaurantDTO get(Long id) {
        return mapper.toDTO(getRestaurantEntity(id));
    }

    public List<RestaurantDTO> getAllWithRatingByDate(Pageable pageable, RestaurantSearchRequest restaurantSearchRequest) {
        return getAllWithRatingByDate(LocalDate.now(), pageable, restaurantSearchRequest);
    }

    public List<RestaurantDTO> getAllWithRatingByDate(LocalDate date, Pageable pageable, RestaurantSearchRequest restaurantSearchRequest) {
        Map<Long, Long> ratingByRestaurant = voteRepository.findAllByVotingDateTimeBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX))
                .stream()
                .collect(Collectors.groupingBy(vote -> vote.getRestaurant().getId(), Collectors.counting()));
        if (restaurantSearchRequest.getRestaurantName() == null && restaurantSearchRequest.getDescription() == null) {
            return mapper.toDTOs(restaurantRepository.findAll(pageable).getContent(), ratingByRestaurant);
        } else {
            return mapper.toDTOs(restaurantRepository
                    .findAll(new RestaurantSpecificationBuilder()
                            .build(restaurantSearchRequest), pageable).getContent(), ratingByRestaurant);
        }
    }

    public List<RestaurantDTO> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return mapper.toDTOs(restaurants);
    }

    public boolean isRestaurantNameAlreadyExists(RestaurantSavingRequestDTO dto) {
        return !Optional.empty().equals(restaurantRepository.findByRestaurantName(dto.getRestaurantName()));
    }

    private Restaurant getRestaurantEntity(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with such ID not found"));
    }
}
