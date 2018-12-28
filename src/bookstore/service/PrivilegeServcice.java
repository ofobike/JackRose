package bookstore.service;

import bookstore.domain.User;

import java.sql.SQLException;

public interface PrivilegeServcice {
    User login(String username, String password) throws SQLException;
}
