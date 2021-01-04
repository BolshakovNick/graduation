package ru.bolshakov.internship.dishes_rating.model.jdbc;

import ru.bolshakov.internship.dishes_rating.model.Role;

public class User extends BaseEntity {
    private String userName;

    private String email;

    private String password;

    private Role role;

    public User(String userName, String email, String password) {
        this(null, userName, email, password, Role.USER);
    }

    public User(Long id, String userName, String email, String password) {
        this(id, userName, email, password, Role.USER);
    }

    public User(String userName, String email, String password, Role role) {
        this(null, userName, email, password, role);
    }

    public User(Long id, String userName, String email, String password, Role role) {
        super(id);
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
