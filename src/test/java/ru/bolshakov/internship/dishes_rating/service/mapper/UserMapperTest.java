package ru.bolshakov.internship.dishes_rating.service.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bolshakov.internship.dishes_rating.data.jpa.TestUserData;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;

import java.util.Arrays;
import java.util.List;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    private final TestUserData data = new TestUserData();

    @Test
    void toUserDTO() {
        UserDTO mappedUser = mapper.toDTO(data.TEST_USER_1);
        Assertions.assertEquals(new UserDTO("name1", "mail1@host.domain", "password1", "USER"), mappedUser);
    }

    @Test
    void toUsersDTO() {
        List<UserDTO> expectedUsers = Arrays.asList(new UserDTO("name1", "mail1@host.domain", "password1", "USER"),
                new UserDTO("name2", "mail2@host.domain", "password2", "USER"),
                new UserDTO("name3", "mail3@host.domain", "password3", "USER"));
        List<UserDTO> usersDTO = mapper.toDTOs(data.TEST_USERS);
        Assertions.assertEquals(expectedUsers, usersDTO);
    }

    @Test
    void toEntityFromRegistrationRequestDTO() {
        User mappedUser = mapper.toEntity(new UserSavingRequestDTO("name1", "mail1@host.domain", "password1"));
        Assertions.assertEquals(data.TEST_USER_1, mappedUser);
    }

    @Test
    void toEntityFromUserDTO() {
        User mappedUser = mapper.toEntity(new UserDTO("name1", "mail1@host.domain", "password1", "USER"));
        Assertions.assertEquals(data.TEST_USER_1, mappedUser);
    }
}