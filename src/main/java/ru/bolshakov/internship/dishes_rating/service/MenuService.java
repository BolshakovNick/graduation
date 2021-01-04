package ru.bolshakov.internship.dishes_rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.Dish;
import ru.bolshakov.internship.dishes_rating.model.jpa.Menu;
import ru.bolshakov.internship.dishes_rating.model.jpa.Restaurant;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaDishRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaMenuRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.DishMapper;
import ru.bolshakov.internship.dishes_rating.service.mapper.MenuMapper;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MenuService {
    protected final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final JpaMenuRepository menuRepository;

    private final JpaDishRepository dishRepository;

    private final JpaRestaurantRepository restaurantRepository;

    private final DishMapper dishMapper;

    private final MenuMapper menuMapper;

    public MenuService(JpaMenuRepository menuRepository,
                       JpaDishRepository dishRepository,
                       JpaRestaurantRepository restaurantRepository,
                       DishMapper dishMapper,
                       MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishMapper = dishMapper;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public DishDTO addDish(DishSavingRequestDTO dto, Long restaurantId) {
        Dish createdDish = dishRepository.save(dishMapper.toEntity(dto, getCurrentMenu(restaurantId)));
        return dishMapper.toDTO(createdDish);
    }

    @Transactional
    public DishDTO updateDish(DishSavingRequestDTO dto, Long restaurantId, Long dishId) {
        Menu currentMenu = getCurrentMenu(restaurantId);
        if (!isDishBelongsMenu(dishId, currentMenu)) {
            throw new NotFoundException("Dish with such id does not exist in current menu");
        }
        Dish updatedDish = dishRepository.save(dishMapper.toEntity(dto, currentMenu, dishId));
        return dishMapper.toDTO(updatedDish);
    }

    @Transactional
    public void deleteDish(Long restaurantId, Long dishId) {
        Menu currentMenu = getCurrentMenu(restaurantId);
        if (!isDishBelongsMenu(dishId, currentMenu)) {
            throw new NotFoundException("Dish with such id does not exist in current menu");
        }
        try {
            dishRepository.deleteById(dishId);
            log.info("Deleting {} successfully completed.", dishId);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Deleting failed. Dish with ID {} not found", dishId);
            throw new NotFoundException("Dish with such ID not found");
        }
    }

    public DishDTO getDish(Long restaurantId, Long dishId) {
        Dish returnedDish = dishRepository.getById(dishId)
                .orElseThrow(() -> new NotFoundException("Dish with such ID is not found"));
        if (!returnedDish.getMenu().equals(getCurrentMenu(restaurantId))) {
            throw new NotFoundException("Dish with such id does not exist in current menu");
        }
        return dishMapper.toDTO(returnedDish);
    }

    @Transactional(readOnly = true)
    public MenuDTO getMenuByRestaurantId(Long restaurantId) {
        Menu currentMenu = getCurrentMenu(restaurantId);
        Long price = Math.round(currentMenu.getDishes().stream().mapToLong(Dish::getPrice).average().orElse(0));
        return menuMapper.toDTO(currentMenu, price);
    }

    private boolean isDishBelongsMenu(Long dishId, Menu menu) {
        Dish dish = dishRepository.getById(dishId)
                .orElseThrow(() -> new NotFoundException("Dish with id " + dishId + " not found"));
        return dish.getMenu().equals(menu);
    }

    private Menu getCurrentMenu(Long restaurantId) {
        Optional<Menu> menu = menuRepository.findByRestaurant_IdAndMenuDate(restaurantId, LocalDate.now());
        if (menu.isEmpty()) {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new NotFoundException("Restaurant with such id not found"));
            return menuRepository.save(new Menu(LocalDate.now(), restaurant));
        } else {
            return menu.get();
        }
    }
}