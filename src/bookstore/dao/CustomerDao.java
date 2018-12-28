package bookstore.dao;

import bookstore.domain.Customer;

import java.sql.SQLException;

public interface CustomerDao {
    /**
     * 用户注册
     * @param customer
     * @return
     */
    String registerCustomer(Customer customer);

    /**
     * 检测用户名是否存在
     * @param username
     * @return
     */
    Customer checkUserName(String username) throws SQLException;

    /**
     * 检测邮箱是否存在
     * @param email
     * @return
     */
    Customer checkEmail(String email) throws SQLException;

    /**
     * 根据激活码查询账户
     * @param code
     * @return
     */
    Customer findUserBycode(String code);

    /**
     * 根据code查询到的用户激活账户
     * @param customer
     * @return
     */
    String checkUserActive(Customer customer) throws SQLException;

    /**
     * 用户登录的方法
     * @param username
     * @param password
     * @return
     */
    Customer findCustomerByUserNameAndPassword(String username, String password) ;
}
