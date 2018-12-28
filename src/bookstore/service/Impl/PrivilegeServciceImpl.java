package bookstore.service.Impl;

import bookstore.dao.Impl.PrivilegeDaoImpl;
import bookstore.dao.PrivilegeDao;
import bookstore.domain.User;
import bookstore.service.PrivilegeServcice;

import java.sql.SQLException;

public class PrivilegeServciceImpl implements PrivilegeServcice {
    private PrivilegeDao privilegeDao = new PrivilegeDaoImpl();
    @Override
    public User login(String username, String password) throws SQLException {
        return privilegeDao.findUser(username,password);
    }
}
