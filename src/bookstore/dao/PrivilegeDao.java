package bookstore.dao;

import bookstore.domain.User;

import java.sql.SQLException;

public interface PrivilegeDao {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    User findUser(String username,String password) throws SQLException;
}
