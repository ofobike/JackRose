package bookstore.dao.Impl;

import bookstore.dao.CustomerDao;
import bookstore.domain.Customer;
import bookstore.utils.JDCBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {
    QueryRunner queryRunner = new QueryRunner(JDCBUtils.getDataSource());

    @Override
    public String registerCustomer(Customer customer) {

        try {
            queryRunner.update("insert into customers (id,username,password,phone,address,email,code,actived) values (?,?,?,?,?,?,?,?)",
                    customer.getId(), customer.getUsername(), customer.getPassword(), customer.getPhone(), customer.getAddress(),
                    customer.getEmail(), customer.getCode(), customer.isActived());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "用户注册失败再数据层出错";

    }

    /**
     * 检测用户是否存在
     *
     * @param username
     * @return
     */
    @Override
    public Customer checkUserName(String username) throws SQLException {
        String sql = "select * from customers where username = ?";
        Customer query = queryRunner.query(sql, new BeanHandler<Customer>(Customer.class), username);
        return query;
    }

    @Override
    public Customer checkEmail(String email) throws SQLException {
        String sql = "select * from customers where email = ?";
        Customer query = queryRunner.query(sql, new BeanHandler<Customer>(Customer.class), email);
        return query;
    }

    /**
     * 根据激活码查询用户
     *
     * @param code
     * @return
     */
    @Override
    public Customer findUserBycode(String code) {
        /**
         * 如果没有查询到返回null
         */
        Customer customer = null;
        try {
            String sql = "select * from customers where code = ?";
            customer = queryRunner.query(sql, new BeanHandler<Customer>(Customer.class), code);
            return customer;
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * 激活账户就是把数据库里的数据类型bit
     *
     * @param customer
     * @return
     */
    @Override
    public String checkUserActive(Customer customer) throws SQLException {
        String sql = "UPDATE  customers SET actived = TRUE WHERE CODE = ?";
        int update = queryRunner.update(sql, customer.getCode());
        if (update > 0) {
            return null;
        }
        return "激活失败！";
    }

    /**
     * 用户登录的方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public Customer findCustomerByUserNameAndPassword(String username, String password)  {
        Customer query = null;
        try {
            String sql = "select * from customers where username = ? and password = ?";
            query = queryRunner.query(sql, new BeanHandler<Customer>(Customer.class), username, password);
            return query;
        } catch (SQLException e) {

        }
        return  null;
    }
}
