package bookstore.service;

import bookstore.commons.Page;
import bookstore.domain.Book;
import bookstore.domain.Category;
import bookstore.domain.Customer;
import bookstore.domain.Order;

import java.sql.SQLException;
import java.util.List;

public interface BusinessService {
    /**
     * 根据分类名称查询该分类是否可用
     * @param categoryName
     * @return
     */
    boolean isCategoryExists(String categoryName) throws SQLException;

    /**
     * 添加书籍分类
     * @param name
     * @param des
     * @return
     */
    String addCategory(String name, String des);

    /**
     * 查询所有的分类
     * @return
     */
    List<Category> findAllCategory() throws SQLException;

    /**
     * 根据id删除分类
     * @param cid
     * @return
     */
    String deleteCategoryById(String cid) throws SQLException;

    /**
     * 根据Id查询分类
     * @param cid
     * @return
     */
    Category findCategoryById(String cid) throws SQLException;

    /**
     * 修改分类
     * @param category
     * @return
     */
    String updateCategory(Category category);

    /**
     * 添加书籍
     * @param book
     * @return
     */
    String addBook(Book book);

    /**
     * 分业查询数据
     * @param num
     * @return
     */
    Page findPage(int num) throws SQLException;

    /**
     * 根据id查询到书籍的数据
     * @param bookId
     * @return
     */
    Book findBookById(String bookId) throws SQLException;

    /**
     * 更新书籍
     * @param book
     * @return
     */
    String updateBookByAdmin(Book book) throws SQLException;

    /**
     * 根据Id删除图书
     * @param deleteBookId
     * @return
     */
    String deleteBookById(String deleteBookId);

    /***********************/
    /****用户的操作业务******/
    /***********************/

    /**
     *
     * @param customer
     * @return
     */
    String registerCustomer(Customer customer);

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    Boolean checkUserName(String username) throws SQLException;

    /**
     * 检测邮箱
     * @param email
     * @return
     */
    Boolean checkEmail(String email) throws SQLException;

    /**
     * 激活用户
     * @param code
     * @return
     */
    String activeUser(String code);

    /**
     * 用户登录的方法
     * @param username
     * @param password
     * @return
     */
    Customer findCustomerByUserNameAndPassword(String username, String password);

    /**
     * 分业查询数据
     * @param num
     * @param categoryId
     * @return
     */
    Page<Book> findPage(String num, String categoryId);

    /**
     * 生成订单
     * @param order
     */
    void genOrder(Order order);

    /**
     * 修改订单的状态
     * @param i
     * @param r6_order
     */
    void changeOrderStatus(int i, String r6_order);

    public void updateOrder(Order order);
    public Order findOrderByNum(String ordernum);

    List<Order> findOrdersByCustomerId(String id);
}
