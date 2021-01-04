package ru.bolshakov.internship.dishes_rating.data.jdbc.user;

import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;

import java.util.Arrays;
import java.util.List;

public class TestUserDTOData {
    public final UserSavingRequestDTO TEST_USER_1 = new UserSavingRequestDTO("name1", "mail1@host.domain", "password1");
    public final UserSavingRequestDTO TEST_USER_2 = new UserSavingRequestDTO("name2", "mail2@host.domain", "password2");
    public final UserSavingRequestDTO TEST_USER_3 = new UserSavingRequestDTO("name3", "mail3@host.domain", "password3");
    public final UserSavingRequestDTO NEW_USER = new UserSavingRequestDTO("newName", "newMail@host.domain", "newPassword");

    public final UserSavingRequestDTO ALREADY_EXISTENT_USER = new UserSavingRequestDTO("existentName", "mail3@host.domain", "existentPassword");
    public final UserDTO NON_EXISTENT_USER = new UserDTO(9999L, "newName1", "newMail1@host.domain", "newPassword1", "USER");

    public Long EXISTENT_USER_ID = 1L;

    public final long NON_EXISTENT_ID = 0;

    public final String NON_EXISTENT_MAIL = "non_existent@email.dom";

    public final List<UserSavingRequestDTO> TEST_USERS = Arrays.asList(TEST_USER_1, TEST_USER_2, TEST_USER_3);

}