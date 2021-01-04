package ru.bolshakov.internship.dishes_rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bolshakov.internship.dishes_rating.dto.search.UserSearchRequest;
import ru.bolshakov.internship.dishes_rating.dto.user.SavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UpdatingRequestDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserDTO;
import ru.bolshakov.internship.dishes_rating.dto.user.UserSavingRequestDTO;
import ru.bolshakov.internship.dishes_rating.exception.NonUniqueParamException;
import ru.bolshakov.internship.dishes_rating.exception.NotFoundException;
import ru.bolshakov.internship.dishes_rating.model.jpa.User;
import ru.bolshakov.internship.dishes_rating.repository.jpa.JpaUserRepository;
import ru.bolshakov.internship.dishes_rating.service.mapper.UserMapper;
import ru.bolshakov.internship.dishes_rating.service.specification.builder.UserSpecificationBuilder;

import java.util.List;

@Service
public class UserService {
    protected final Logger log = LoggerFactory.getLogger(UserService.class);

    private final JpaUserRepository repository;

    private final UserMapper mapper;

    private final PasswordService passwordService;

    private final UserAccountVerificationService verificationService;

    public UserService(JpaUserRepository repository,
                       UserMapper mapper,
                       PasswordService passwordService,
                       UserAccountVerificationService verificationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordService = passwordService;
        this.verificationService = verificationService;
    }

    @Transactional
    public UserDTO create(UserSavingRequestDTO request) {
        if (isUserExists(request)) {
            throw new NonUniqueParamException("Email must be unique");
        }
        User entity = mapper.toEntity(request);
        entity.setPassword(passwordService.encode(request.getPassword()));
        User returnedUser = repository.save(entity);
        verificationService.performUserVerification(mapper.toAuthRequest(request));
        return mapper.toDTO(returnedUser);
    }

    @Transactional
    public UserDTO update(Long userId, UserSavingRequestDTO requestDTO) {
        return update(userId, mapper.toUpdateRequest(requestDTO));
    }

    @Transactional
    public UserDTO update(Long userId, UpdatingRequestDTO requestDTO) {
        User entity = getUserEntity(userId);
        entity.setPassword(passwordService.encode(requestDTO.getPassword()));
        entity.setEmail(requestDTO.getEmail());
        entity.setUserName(requestDTO.getUserName());
        entity.setRole(requestDTO.getRole());
        User returnedUser = repository.save(entity);
        return mapper.toDTO(returnedUser);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Deleting {} failed. Possible reason: user with id {} does not exist in database", id, id);
            throw new NotFoundException("User with such ID is not found");
        }
    }

    public UserDTO get(Long id) {
        return mapper.toDTO(getUserEntity(id));
    }

    public UserDTO getByEmail(String email) {
        User returnedUser = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with such email is not found"));
        return mapper.toDTO(returnedUser);
    }

    public List<UserDTO> getAll(Pageable pageable, UserSearchRequest request) {
        if (request.getUserName() == null) {
            return mapper.toDTOs(repository.findAll(pageable).getContent());
        } else {
            UserSpecificationBuilder builder = new UserSpecificationBuilder();
            return mapper.toDTOs(repository.findAll(builder.build(request), pageable).getContent());
        }
    }

    public boolean isUserExists(SavingRequestDTO user) {
        try {
            getByEmail(user.getEmail());
            return true;
        } catch (NotFoundException exception) {
            return false;
        }
    }

    private User getUserEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User with such ID is not found"));
    }
}