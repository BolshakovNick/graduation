package ru.bolshakov.internship.dishes_rating.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.bolshakov.internship.dishes_rating.dto.search.SearchRequest;
import ru.bolshakov.internship.dishes_rating.dto.user.AuthorizationRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UpdatingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.NonUniqueParamException;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.Role;
import ru.bolshakov.internship.dishes_rating.model.User;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.UserMapper;
import ru.bolshakov.internship.dishes_rating.service.specification.SearchOperationType;
import ru.bolshakov.internship.dishes_rating.service.specification.UserSpecification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final User NEW_USER = new User("newName", "newMail@host.domain", "newPassword");
    private static final User EXISTENT_USER = new User(1L, NEW_USER.getUserName(), NEW_USER.getEmail(), NEW_USER.getPassword(), NEW_USER.getRole());
    private static final UserSavingRequestDTO REGISTRATION_REQUEST_DTO = new UserSavingRequestDTO(NEW_USER.getUserName(), NEW_USER.getEmail(), NEW_USER.getPassword());
    private static final UpdatingRequestDTO UPDATING_REQUEST_DTO = new UpdatingRequestDTO(NEW_USER.getUserName(), NEW_USER.getEmail(), NEW_USER.getPassword(), Role.USER);
    private static final Long EXISTENT_USER_ID = 1L;
    private static final List<User> EXISTENT_USERS = new ArrayList<>();

    static {
        EXISTENT_USERS.add(new User(5L, "username5", "user5@host.domain", "password5"));
        EXISTENT_USERS.add(new User(6L, "username6", "user6@host.domain", "password6"));
        EXISTENT_USERS.add(new User(7L, "username7", "user7@host.domain", "password7"));
    }

    @Mock
    private JpaUserRepository repository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserAccountVerificationService verificationService;

    @Spy
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void create() {
        Mockito.when(repository.save(NEW_USER))
                .thenReturn(EXISTENT_USER);
        Mockito.doNothing().when(verificationService).performUserVerification(new AuthorizationRequestDTO(NEW_USER.getEmail(), NEW_USER.getPassword()));
        Mockito.when(mapper.toAuthRequest(REGISTRATION_REQUEST_DTO))
                .thenReturn(new AuthorizationRequestDTO(NEW_USER.getEmail(), NEW_USER.getPassword()));

        UserDTO createdUserDTO = service.create(REGISTRATION_REQUEST_DTO);
        assertNotNull(createdUserDTO.getId());
        assertEquals(NEW_USER.getUserName(), createdUserDTO.getUserName());
        assertEquals(NEW_USER.getEmail(), createdUserDTO.getEmail());
    }

    @Test
    void createIfUserAlreadyExists() {
        Mockito.when(repository.findByEmail(NEW_USER.getEmail())).thenReturn(Optional.of(NEW_USER));

        assertThrows(NonUniqueParamException.class, () -> service.create(REGISTRATION_REQUEST_DTO));
    }

    @Test
    void update() {
        String someEncodedPassword = "someEncodedPassword";

        Mockito.when(repository.save(EXISTENT_USER)).thenReturn(EXISTENT_USER);
        Mockito.when(repository.findById(EXISTENT_USER.getId())).thenReturn(Optional.of(EXISTENT_USER));
        Mockito.when(passwordService.encode(EXISTENT_USER.getPassword())).thenReturn(someEncodedPassword);

        UserDTO changedUser = service.update(EXISTENT_USER.getId(), UPDATING_REQUEST_DTO);
        assertEquals(EXISTENT_USER.getId(), changedUser.getId());
        assertEquals(EXISTENT_USER.getUserName(), changedUser.getUserName());
        assertEquals(EXISTENT_USER.getEmail(), changedUser.getEmail());
        assertEquals(someEncodedPassword, changedUser.getPassword());
    }

    @Test
    void updateIfUserDoesNotExist() {
        Mockito.when(repository.findById(EXISTENT_USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(EXISTENT_USER_ID, UPDATING_REQUEST_DTO));
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> service.delete(EXISTENT_USER_ID));

        Mockito.when(repository.findById(EXISTENT_USER_ID)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.get(EXISTENT_USER_ID));
    }

    @Test
    void deleteIfUserDoesNotExist() {
        Mockito.doThrow(NotFoundException.class).when(repository).deleteById(EXISTENT_USER_ID);
        assertThrows(NotFoundException.class, () -> service.delete(EXISTENT_USER_ID));
    }


    @Test
    void get() {
        Mockito.when(repository.findById(EXISTENT_USER.getId())).thenReturn(Optional.of(EXISTENT_USER));

        UserDTO receivedUser = service.get(EXISTENT_USER.getId());
        assertEquals(EXISTENT_USER.getUserName(), receivedUser.getUserName());
        assertEquals(EXISTENT_USER.getEmail(), receivedUser.getEmail());
        assertEquals(EXISTENT_USER.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getIfUserDoesNotExist() {
        Mockito.when(repository.findById(EXISTENT_USER_ID)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.get(EXISTENT_USER_ID));
    }

    @Test
    void getByEmail() {
        Mockito.when(repository.findByEmail(EXISTENT_USER.getEmail())).thenReturn(Optional.of(EXISTENT_USER));

        UserDTO receivedUser = service.getByEmail(EXISTENT_USER.getEmail());
        assertEquals(EXISTENT_USER.getUserName(), receivedUser.getUserName());
        assertEquals(EXISTENT_USER.getEmail(), receivedUser.getEmail());
        assertEquals(EXISTENT_USER.getPassword(), receivedUser.getPassword());
    }

    @Test
    void getByEmailIfUserDoesNotExist() {
        String email = "nonexistent@mail.com";
        Mockito.when(repository.findByEmail(email)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.getByEmail(email));
    }

    @Test
    void getAll() {
        PageRequest pageRequest = PageRequest.of(0, 3);

        Mockito.when(repository.findAll(pageRequest)).thenReturn(new PageImpl<>(EXISTENT_USERS));

        List<UserDTO> users = service.getAll(pageRequest, new SearchRequest());
        assertEquals(3, users.size());

        for (int i = 0; i < users.size(); i++) {
            assertEquals(EXISTENT_USERS.get(i).getEmail(), users.get(i).getEmail());
        }
    }

    @Test
    void getAllFilteredByLike() {
        SearchRequest searchRequest = new SearchRequest("userName", EXISTENT_USER.getUserName(), SearchOperationType.LIKE);
        searchRequest.setParameter("userName");
        searchRequest.setValue(EXISTENT_USER.getUserName());
        searchRequest.setOperator(SearchOperationType.LIKE);

        Mockito.when(repository.findAll(Mockito.any(UserSpecification.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(EXISTENT_USER)));

        List<UserDTO> users = service.getAll(PageRequest.of(0, 3), searchRequest);
        assertEquals(1, users.size());

        assertEquals(EXISTENT_USER.getEmail(), users.get(0).getEmail());
    }

    @Test
    void getAllFilteredByStartWith() {
        SearchRequest searchRequest = new SearchRequest("userName", "user", SearchOperationType.STARTS);

        Mockito.when(repository.findAll(Mockito.any(UserSpecification.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(EXISTENT_USERS));

        List<UserDTO> users = service.getAll(PageRequest.of(0, 3), searchRequest);
        assertEquals(3, users.size());

        for (int i = 0; i < users.size(); i++) {
            assertEquals(EXISTENT_USERS.get(i).getEmail(), users.get(i).getEmail());
        }
    }

    @Test
    void getAllFilteredByEndWith() {
        SearchRequest searchRequest = new SearchRequest("userName", "1", SearchOperationType.ENDS);
        searchRequest.setParameter("userName");
        searchRequest.setValue("1");

        Mockito.when(repository.findAll(Mockito.any(UserSpecification.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(EXISTENT_USER)));

        List<UserDTO> users = service.getAll(PageRequest.of(0, 3), searchRequest);
        assertEquals(1, users.size());

        assertEquals(EXISTENT_USER.getEmail(), users.get(0).getEmail());
    }

    @Test
    void isUserExists() {
        Mockito.when(repository.findByEmail(REGISTRATION_REQUEST_DTO.getEmail())).thenReturn(Optional.of(EXISTENT_USER));
        assertTrue(service.isUserExists(REGISTRATION_REQUEST_DTO));
    }

    @Test
    void isUserExistsIfUserDoesNotExist() {
        Mockito.when(repository.findByEmail(REGISTRATION_REQUEST_DTO.getEmail())).thenReturn(Optional.empty());
        assertFalse(service.isUserExists(REGISTRATION_REQUEST_DTO));
    }
}
