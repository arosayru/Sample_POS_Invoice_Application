package com.pos.controller;

import com.pos.dao.UserDAO;
import com.pos.model.User;
import java.sql.SQLException;

public class UserController {
    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password are required!");
        }
        return userDAO.login(username, password);
    }
}
