package bookstore.dao.Impl;

import bookstore.dao.PrivilegeDao;
import bookstore.domain.User;
import bookstore.utils.JDCBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class PrivilegeDaoImpl implements PrivilegeDao {
    private QueryRunner queryRunner = new QueryRunner(JDCBUtils.getDataSource());
    @Override
    public User findUser(String username, String password) throws SQLException {
        String sql="SELECT * FROM users WHERE username =? AND PASSWORD =?";
        return queryRunner.query(sql,new BeanHandler<User>(User.class),username,password);
    }
}
