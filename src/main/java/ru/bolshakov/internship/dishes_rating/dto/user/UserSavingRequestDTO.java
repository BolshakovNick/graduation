package ru.bolshakov.internship.dishes_rating.dto.user;

public class UserSavingRequestDTO extends SavingRequestDTO {

    public UserSavingRequestDTO(String userName, String email, String password) {
        super(userName, email, password);
    }
}
