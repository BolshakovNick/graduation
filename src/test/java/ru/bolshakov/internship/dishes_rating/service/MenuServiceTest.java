package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishDTO;
import ru.bolshakov.internship.dishes_rating.dto.dish.DishSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.menu.MenuDTO;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Dish;
import ru.bolshakov.internship.dishes_rating.model.Menu;
import ru.bolshakov.internship.dishes_rating.model.Restaurant;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaDishRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaMenuRepository;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaRestaurantRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.DishMapper;
import ru.bolshakov.internship.dishes_rating.service.mapper.MenuMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private static final LocalDate NOW = LocalDate.now();
    private static final Restaurant EXISTENT_RESTAURANT = new Restaurant(20L, "restaurant1", "description1");
    private static final Menu EXISTENT_MENU = new Menu(50L, NOW, EXISTENT_RESTAURANT);
    private static final Menu ANOTHER_MENU = new Menu(1000L, NOW, EXISTENT_RESTAURANT);
    private static final Dish NEW_DISH = new Dish("dish1", 10000L, EXISTENT_MENU);
    private static final Dish EXISTENT_DISH = new Dish(1L, "dish1", 10000L, EXISTENT_MENU);

    static {
        List<Dish> dishes = Arrays.asList(
                new Dish(1L, "name1", 10000L, EXISTENT_MENU),
                new Dish(2L, "name2", 15000L, EXISTENT_MENU),
                new Dish(3L, "name3", 10050L, EXISTENT_MENU));
        EXISTENT_MENU.setDishes(new HashSet<>(dishes));
    }

    @Mock
    private JpaMenuRepository menuRepository;

    @Mock
    private JpaDishRepository dishRepository;

    @Mock
    private JpaRestaurantRepository restaurantRepository;

    @Spy
    private final DishMapper dishMapper = new DishMapper();

    @Spy
    private final MenuMapper menuMapper = new MenuMapper(dishMapper);

    @InjectMocks
    MenuService service;

    @Test
    void createDish() {
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(EXISTENT_MENU));
        when(dishRepository.save(NEW_DISH))
                .thenReturn(EXISTENT_DISH);

        DishDTO createdDishDTO = service.addDish(new DishSavingRequestDTO(NEW_DISH.getName(), NEW_DISH.getPrice()), EXISTENT_RESTAURANT.getId());
        assertNotNull(createdDishDTO.getId());
        assertEquals(NEW_DISH.getName(), createdDishDTO.getName());
        assertEquals(NEW_DISH.getPrice(), createdDishDTO.getPrice());
        assertEquals(NEW_DISH.getMenu().getId(), createdDishDTO.getMenuId());
    }

    @Test
    void createDishIfMenuDoesNotExist() {
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.empty());
        when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId()))
                .thenReturn(Optional.of(EXISTENT_RESTAURANT));
        when(menuRepository.save(new Menu(NOW, EXISTENT_RESTAURANT)))
                .thenReturn(EXISTENT_MENU);
        when(dishRepository.save(NEW_DISH))
                .thenReturn(EXISTENT_DISH);

        DishDTO dishDTO = service.addDish(new DishSavingRequestDTO(NEW_DISH.getName(), NEW_DISH.getPrice()), EXISTENT_RESTAURANT.getId());

        assertNotNull(dishDTO.getId());
        assertEquals(NEW_DISH.getName(), dishDTO.getName());
        assertEquals(NEW_DISH.getPrice(), dishDTO.getPrice());
        assertEquals(EXISTENT_MENU.getId(), dishDTO.getMenuId());
    }

    @Test
    void updateDish() {
        when(dishRepository.save(EXISTENT_DISH)).thenReturn(EXISTENT_DISH);
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(EXISTENT_MENU));
        when(dishRepository.getById(EXISTENT_DISH.getId())).thenReturn(Optional.of(EXISTENT_DISH));

        DishDTO createdDish = service.updateDish(new DishSavingRequestDTO(EXISTENT_DISH.getName(), EXISTENT_DISH.getPrice()), EXISTENT_RESTAURANT.getId(), EXISTENT_DISH.getId());
        assertNotNull(createdDish.getId());
        assertEquals(EXISTENT_DISH.getName(), createdDish.getName());
        assertEquals(EXISTENT_DISH.getPrice(), createdDish.getPrice());
        assertEquals(EXISTENT_DISH.getMenu().getId(), createdDish.getMenuId());
    }

    @Test
    void updateIfDishDoesNotExist() {
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(new Menu(EXISTENT_MENU.getId(), NOW, EXISTENT_RESTAURANT)));
        when(dishRepository.getById(NEW_DISH.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateDish(new DishSavingRequestDTO(NEW_DISH.getName(), NEW_DISH.getPrice()), EXISTENT_RESTAURANT.getId(), NEW_DISH.getId()));
    }

    @Test
    void updateIfDishDoesNotBelongMenu() {
        when(dishRepository.getById(NEW_DISH.getId())).thenReturn(Optional.of(EXISTENT_DISH));
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(ANOTHER_MENU));

        assertThrows(NotFoundException.class, () -> service.updateDish(new DishSavingRequestDTO(NEW_DISH.getName(), NEW_DISH.getPrice()), EXISTENT_RESTAURANT.getId(), NEW_DISH.getId()));
    }

    @Test
    void deleteDish() {
        Mockito.doNothing().when(dishRepository).deleteById(EXISTENT_DISH.getId());
        when(dishRepository.getById(EXISTENT_DISH.getId())).thenReturn(Optional.of(EXISTENT_DISH));
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(EXISTENT_MENU));
        assertDoesNotThrow(() -> service.deleteDish(EXISTENT_RESTAURANT.getId(), EXISTENT_DISH.getId()));
    }

    @Test
    void deleteIfDishDoesNotBelongMenu() {
        when(dishRepository.getById(EXISTENT_DISH.getId())).thenReturn(Optional.of(EXISTENT_DISH));
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(ANOTHER_MENU));

        assertThrows(NotFoundException.class, () -> service.deleteDish(EXISTENT_RESTAURANT.getId(), EXISTENT_DISH.getId()));
    }

    @Test
    void getDish() {
        when(dishRepository.getById(EXISTENT_DISH.getId())).thenReturn(Optional.of(EXISTENT_DISH));
        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), NOW))
                .thenReturn(Optional.of(new Menu(EXISTENT_DISH.getMenu().getId(), NOW, EXISTENT_RESTAURANT)));

        DishDTO returnedDish = service.getDish(EXISTENT_RESTAURANT.getId(), EXISTENT_DISH.getId());

        assertEquals(EXISTENT_DISH.getName(), returnedDish.getName());
        assertEquals(EXISTENT_DISH.getPrice(), returnedDish.getPrice());
        assertEquals(EXISTENT_DISH.getMenu().getId(), returnedDish.getMenuId());
    }

    @Test
    void getIfDishDoesNotExist() {
        when(dishRepository.getById(EXISTENT_DISH.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getDish(1L, EXISTENT_DISH.getId()));
    }

    @Test
    void getMenuByRestaurantId() {
        Long expectedAveragePrice = Math.round(EXISTENT_MENU.getDishes().stream().mapToLong(Dish::getPrice).average().orElse(0));
        String expectedFormattedAveragePrice = PriceFormatter.doPriceFormatting(expectedAveragePrice);

        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), LocalDate.now())).thenReturn(Optional.of(EXISTENT_MENU));

        MenuDTO menuDTO = service.getMenuByRestaurantId(EXISTENT_RESTAURANT.getId());

        assertEquals(EXISTENT_MENU.getRestaurant().getId(), menuDTO.getRestaurantId());
        assertEquals(EXISTENT_MENU.getMenuDate(), menuDTO.getMenuDate());
        assertEquals(expectedAveragePrice, menuDTO.getAveragePrice());
        assertEquals(expectedFormattedAveragePrice, menuDTO.getFormattedAveragePrice());

        List<Dish> dishes = new ArrayList<>(EXISTENT_MENU.getDishes());
        assertEquals(dishes.get(0).getId(), menuDTO.getDishes().get(0).getId());
        assertEquals(dishes.get(1).getId(), menuDTO.getDishes().get(1).getId());
        assertEquals(dishes.get(2).getId(), menuDTO.getDishes().get(2).getId());
    }

    @Test
    void getMenuIfDoesNotExist() {
        MenuDTO menuDTO = new MenuDTO(EXISTENT_MENU.getMenuDate(), EXISTENT_MENU.getRestaurant().getId(), 0L, "0.00", new ArrayList<>());
        Menu menu = new Menu(EXISTENT_MENU.getId(), EXISTENT_MENU.getMenuDate(), EXISTENT_RESTAURANT);
        menu.setDishes(new HashSet<>());

        when(menuRepository.findByRestaurant_IdAndMenuDate(EXISTENT_RESTAURANT.getId(), LocalDate.now())).thenReturn(Optional.empty());
        when(menuRepository.save(new Menu(EXISTENT_MENU.getMenuDate(), EXISTENT_MENU.getRestaurant())))
                .thenReturn(menu);
        when(restaurantRepository.findById(EXISTENT_RESTAURANT.getId())).thenReturn(Optional.of(EXISTENT_RESTAURANT));

        MenuDTO menuByRestaurantId = service.getMenuByRestaurantId(EXISTENT_RESTAURANT.getId());

        assertEquals(menuDTO.getMenuDate(), menuByRestaurantId.getMenuDate());
        assertEquals(menuDTO.getRestaurantId(), menuByRestaurantId.getRestaurantId());
        assertEquals(menuDTO.getDishes(), menuByRestaurantId.getDishes());
    }
}
