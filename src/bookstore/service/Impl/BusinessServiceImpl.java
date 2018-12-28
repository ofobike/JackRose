package bookstore.service.Impl;

import bookstore.commons.Page;
import bookstore.dao.BookDao;
import bookstore.dao.CategoryDao;
import bookstore.dao.CustomerDao;
import bookstore.dao.Impl.BookDaoImpl;
import bookstore.dao.Impl.CategoryImpl;
import bookstore.dao.Impl.CustomerDaoImpl;
import bookstore.dao.Impl.OrderDaoImpl;
import bookstore.dao.OrderDao;
import bookstore.domain.Book;
import bookstore.domain.Category;
import bookstore.domain.Customer;
import bookstore.domain.Order;
import bookstore.service.BusinessService;
import bookstore.utils.MailUtils;
import bookstore.utils.StringUtils;
import bookstore.utils.ValidateUserInfo;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BusinessServiceImpl implements BusinessService {
    private CategoryDao categoryDao = new CategoryImpl();
    private BookDao bookDao = new BookDaoImpl();
    private CustomerDao customerDao = new CustomerDaoImpl();
    private OrderDao orderDao = new OrderDaoImpl();

    /**
     * 根据名字查询分类是否可以使用
     *
     * @param categoryName
     * @return
     */
    @Override
    public boolean isCategoryExists(String categoryName) throws SQLException {
        Category category = categoryDao.findByName(categoryName);
        return category == null ? false : true;
    }

    /**
     * 添加书籍分类
     *
     * @param name
     * @param des
     * @return
     */
    @Override
    public String addCategory(String name, String des) {
        if ((name == null || "".equals(name)) && (des == null || "".equals(des))) {
            return "数据不能为空";
        }
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(name);
        category.setDes(des);
        return categoryDao.addCategory(category);
    }

    /**
     * 查询所有的分类
     *
     * @return
     */
    @Override
    public List<Category> findAllCategory() throws SQLException {
        return categoryDao.findAllResult();

    }

    /**
     * 根据Id删除
     *
     * @param cid
     * @return
     */
    @Override
    public String deleteCategoryById(String cid) throws SQLException {
        return categoryDao.deleteCategoryById(cid);
    }

    @Override
    public Category findCategoryById(String cid) throws SQLException {
        return categoryDao.findCategoryById(cid);
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @Override
    public String updateCategory(Category category) {
        return categoryDao.updateCategory(category);
    }

    @Override
    public String addBook(Book book) {
        if (book == null) {
            return "没有添加数据";
        }
        if (book.getCategory() == null) {
            return "没有选择分类";
        }
        book.setId(UUID.randomUUID().toString());
        return bookDao.addBook(book);
    }

    /**
     * 分业的逻辑业务处理
     *
     * @param num
     * @return
     */
    @Override
    public Page findPage(int num) throws SQLException {
        //获取总的记录条数
        int totalRecordsNum = bookDao.getTotalRecordsNum();
        Page<Book> page = new Page<>(num, totalRecordsNum);
        List<Book> list = null;
        //分业查询
        if(page.getStartIndex()<0){
            int StartIndex = 0;
            list = bookDao.findPageBooks(page.getStartIndex(), page.getPageSize());
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 根据id查询分类的数据
     *
     * @param bookId
     * @return
     */
    @Override
    public Book findBookById(String bookId) throws SQLException {
        return bookDao.findBookById(bookId);
    }

    /**
     * 更新书籍
     *
     * @param book
     * @return
     */
    @Override
    public String updateBookByAdmin(Book book) throws SQLException {
        return bookDao.updateBookByAdmin(book);
    }

    /**
     * 根据ID删除
     *
     * @param deleteBookId
     * @return
     */
    @Override
    public String deleteBookById(String deleteBookId) {
        return bookDao.deleteBookById(deleteBookId);
    }

    /**
     * .::::.
     * .::::::::.
     * :::::::::::  FUCK YOU
     * ..:::::::::::'
     * '::::::::::::'
     * .::::::::::
     * '::::::::::::::..
     * ..::::::::::::.
     * ``::::::::::::::::
     * ::::``:::::::::'        .:::.
     * ::::'   ':::::'       .::::::::.
     * .::::'      ::::     .:::::::'::::.
     * .:::'       :::::  .:::::::::' ':::::.
     * .::'        :::::.:::::::::'      ':::::.
     * .::'         ::::::::::::::'         ``::::.
     * ...:::           ::::::::::::'              ``::.
     * ```` ':.          ':::::::::'                  ::::..
     * '.:::::'                    ':'````..
     */

    @Override
    public String registerCustomer(Customer customer) {
        String mess = ValidateUserInfo.validate(customer.getUsername(), customer.getPassword(), customer.getPhone(), customer.getAddress(), customer.getEmail());
        if (mess != null) {
            return mess;
        }
        try {
            //生成字符串
            String code = StringUtils.gengraString();
            customer.setCode(code);
            customer.setId(StringUtils.gengraStringWith());
            String result = customerDao.registerCustomer(customer);
            //再注册成功的时候发送一份注册邮件
            MailUtils mailUtils = new MailUtils(customer.getEmail(), code);
        } catch (Exception e) {
            e.printStackTrace();
            return "注册失败";
        }
        return null;
    }

    @Override
    public Boolean checkUserName(String username) throws SQLException {

        Customer customer = customerDao.checkUserName(username);
        return customer == null ? false : true;
    }

    @Override
    public Boolean checkEmail(String email) throws SQLException {
        Customer customer = customerDao.checkEmail(email);
        return customer == null ? false : true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public String activeUser(String code) {
        String result = null;
        Customer customer = customerDao.findUserBycode(code);
        if (customer == null) {
            return "系统没有这个账户";
        }
        /**
         * 修改数据库里的active状态
         * 可以使用customer.setActived(true);
         * 那么数据库里的update customers set actived = ? and code = ?
         */
        try {
            result = customerDao.checkUserActive(customer);
            return result;
        } catch (SQLException e) {
        }
        return result;
    }

    /**
     * 用户登录的方法
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Customer findCustomerByUserNameAndPassword(String username, String password) {
        Customer customer = customerDao.findCustomerByUserNameAndPassword(username, password);
        return customer;
    }

    /**
     * 分业查询数据
     *
     * @param num
     * @param categoryId
     * @return
     */
    @Override
    public Page<Book> findPage(String num, String categoryId) {

        int pageNum = 1;
        if (num != null && !"0".equals(num)) {
            pageNum = Integer.parseInt(num);
        }
        //根据分类的id查询书籍的总记录数量
        int totalRecordsNum = bookDao.getTotalRecordsNum(categoryId);

        Page<Book> page = new Page<>(pageNum,totalRecordsNum);
        //分页查询
        List<Book>  records=bookDao.findPageBooks(page.getStartIndex(), page.getPageSize(),categoryId);
        page.setRecords(records);
        return page;
    }

    @Override
    public void genOrder(Order order) {
        if(order==null)
            throw new RuntimeException("订单不能为空");
        if(order.getCustomer()==null)
            throw new RuntimeException("订单的客户不能为空");
        orderDao.save(order);
    }

    @Override
    public void changeOrderStatus(int status, String ordernum) {
        Order order=findOrderByNum(ordernum);
        order.setStatus(status);
        updateOrder(order);
    }

    @Override
    public Order findOrderByNum(String ordernum) {

        return orderDao.findByNum(ordernum);
    }

    @Override
    public List<Order> findOrdersByCustomerId(String id) {
        return orderDao.findByCustomerId(id);
    }

    @Override
    public void updateOrder(Order order) {
        orderDao.update(order);

    }

}
