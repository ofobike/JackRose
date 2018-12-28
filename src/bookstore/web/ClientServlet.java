package bookstore.web;

import bookstore.commons.Cart;
import bookstore.commons.CartItem;
import bookstore.commons.Page;
import bookstore.domain.*;
import bookstore.service.BusinessService;
import bookstore.service.Impl.BusinessServiceImpl;
import bookstore.service.Impl.PrivilegeServciceImpl;
import bookstore.service.PrivilegeServcice;
import bookstore.utils.OrderNumUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/servlet/ClientServlet")
public class ClientServlet extends BaseServlet {
    BusinessService businessService = new BusinessServiceImpl();

    /**
     * 展示订单
     *
     * @param request
     * @param response
     */
    public void showOrders(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //检测是否登录；
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.getWriter().write("请先登录");
            response.setHeader("Refresh", "2;URL=" + request.getContextPath());
            return;
        }
        List<Order> orders = businessService.findOrdersByCustomerId(customer.getId());
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/client/listOrders.jsp").forward(request, response);
    }

    /**
     * 结算
     *
     * @param request
     * @param response
     */
    public void genOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //检测是否登录；
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.getWriter().write("请先登录");
            response.setHeader("Refresh", "2;URL=" + request.getContextPath());
            return;
        }
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        Order order = new Order();
        order.setOrdernum(OrderNumUtil.genOrderNum());
        order.setPrice(cart.getPrice());
        order.setNumber(cart.getNumber());
        order.setCustomer(customer);
        List<OrderItem> oItems = new ArrayList<OrderItem>();
        //设置订单项
        for (Map.Entry<String, CartItem> me : cart.getItems().entrySet()) {
            OrderItem item = new OrderItem();
            item.setId(UUID.randomUUID().toString());
            item.setNumber(me.getValue().getNumber());
            item.setPrice(me.getValue().getPrice());
            item.setBook(me.getValue().getBook());
            oItems.add(item);
        }
        //建立和订单的关系
        order.setItems(oItems);
        businessService.genOrder(order);
        request.setAttribute("order", order);
        request.getRequestDispatcher("/client/pay.jsp").forward(request, response);
    }

    /**
     * 修改数量
     *
     * @param request
     * @param response
     */
    public void changeNum(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        Book book = businessService.findBookById(bookId);
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        CartItem item = cart.getItems().get(bookId);
        item.setNumber(Integer.parseInt(request.getParameter("num")));
        response.sendRedirect(request.getContextPath() + "/client/showCart.jsp");
    }

    /**
     * 删除购物车里面的书籍
     *
     * @param request
     * @param response
     */
    public void delOneItem(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        Book book = businessService.findBookById(bookId);
        //获取Session里面
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.getItems().remove(book);
        response.sendRedirect(request.getContextPath() + "/client/showCart.jsp");
    }

    /**
     * 添加购物车
     *
     * @param request
     * @param response
     */
    public void buyBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        //获取书籍的id
        String bookId = request.getParameter("bookId");
        //根据书籍的id查询书籍的信息
        Book book = businessService.findBookById(bookId);
        //从Session中获取user
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            //跳转到登录页面
            request.getRequestDispatcher("/client/login.jsp").forward(request, response);
            return;
        }
        //购物车的设计
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            //重新创建一个购物车
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }
        ////向item添加
        cart.addBook2Items(book);
        //提示
        request.setAttribute("message", "购买成功！<a href='javascript:window.history.back()'>返回</a>");
        request.getRequestDispatcher("/client/message.jsp").forward(request, response);
    }

    /**
     * 根据数据的id展示书籍
     *
     * @param request
     * @param response
     */
    public void listBookByCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Category> allCategory = businessService.findAllCategory();
        request.setAttribute("cs", allCategory);
        String categoryId = request.getParameter("categoryId");
        String num = request.getParameter("num");
        int pageNum = 0;
        if (num != null && !"".equals(num) && num.length() > 0 && !"null".equals(num)) {
            pageNum = Integer.parseInt(num);
        } else {
            pageNum = 1;
        }
        //根据书籍的id查询书籍
        Page<Book> page = businessService.findPage(num, categoryId);
        page.setUrl("/servlet/ClientServlet?method=listBookByCategory&categoryId=" + categoryId);
        request.setAttribute("page", page);
        request.getRequestDispatcher("/client/listBooks.jsp").forward(request, response);
    }


    /**
     * 展示所有的书籍
     *
     * @param request
     * @param response
     */
    public void listBooks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        //查询所有的分类
        List<Category> allCategory = businessService.findAllCategory();
        request.setAttribute("cs", allCategory);
        //查询所有商品的分页数据
        String num = request.getParameter("num");
        int pageNum = 0;
        if (num != null && !"".equals(num) && num.length() > 0 && !"null".equals(num)) {
            pageNum = Integer.parseInt(num);
        } else {
            pageNum = 1;
        }
        Page page = businessService.findPage(pageNum);
        page.setUrl("/servlet/ClientServlet?method=listBooks");
        request.setAttribute("page", page);
        request.getRequestDispatcher("/client/listBooks.jsp").forward(request, response);
    }


    /**
     * 用户注销的方法
     *
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        //跳转到登录的页面
        request.getRequestDispatcher("/client/login.jsp").forward(request, response);
    }

    /**
     * 用户登录的方法
     *
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ((username == null || "".equals(username)) && (password == null || "".equals(password))) {
            request.setAttribute("error", "没有输入数据");
            request.getRequestDispatcher("/client/login.jsp").forward(request, response);
            return;
        }
        BusinessService businessService = new BusinessServiceImpl();
        Customer customer = businessService.findCustomerByUserNameAndPassword(username, password);
        if (customer == null) {
            request.setAttribute("msg", "用户名和密码不正确");
            request.getRequestDispatcher("/client/login.jsp").forward(request, response);
            return;
        }
        boolean actived = customer.isActived();
        //如果为真那么已经激活
        if (!actived) {
            //如果不为真那么就没有激活
            request.setAttribute("msg", "用户没有激活请联系管理员");
            request.getRequestDispatcher("/client/login.jsp").forward(request, response);
            return;
        }
        //如果用户已经登录那么就保存在session
        request.getSession().setAttribute("customer", customer);
        request.getRequestDispatcher("/client/head.jsp").forward(request, response);
    }

    /**
     * <a href='http://localhost:8080/servlet/ClientServlet?method=activeUser&code="
     * + code + "'>
     * 激活用户
     *
     * @param request
     * @param response
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String code = request.getParameter("code");
        BusinessService businessService = new BusinessServiceImpl();
        String result = businessService.activeUser(code);
        if (result == null) {
            //激活成功
            response.sendRedirect(request.getContextPath() + "/client/login.jsp");
        } else {
            //激活失败
            request.setAttribute("error", result);
            request.getRequestDispatcher("/client/login.jsp").forward(request, response);
        }
    }

    /**
     * 检测邮箱是否可以注册
     *
     * @param request
     * @param response
     */
    public void checkEmal(HttpServletRequest request, HttpServletResponse response) {
        try {
            String email = request.getParameter("email");
            if (email == null || "".equals(email)) {
                response.getWriter().print("<font color='pink'>没有输入数据</font>");
                return;
            }
            BusinessService businessService = new BusinessServiceImpl();
            Boolean flag = businessService.checkEmail(email);
            if (flag) {
                response.getWriter().print("<font color='red'>该邮箱已经存在</font>");
            } else {
                response.getWriter().print("<font color='green'>该邮箱可以使用</font>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断用户是否存在
     *
     * @param request
     * @param response
     */
    public void checkUsernmae(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            if (username == null || "".equals(username)) {
                response.getWriter().print("<font color='pink'>没有输入数据</font>");
                return;
            }
            BusinessService businessService = new BusinessServiceImpl();
            Boolean flag = businessService.checkUserName(username);
            if (flag) {
                response.getWriter().print("<font color='red'>该用户已经存在</font>");
            } else {
                response.getWriter().print("<font color='green'>该用户可以使用</font>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 用户注册
     *
     * @param request
     */
    public void customerRegist(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            //对获取的参数简单的处理
            String error = "";
            if (username == null || "".equals(username)) {
                error += "用户名为空";
            }
            if (password == null || "".equals(password)) {
                error += "密码为空";
            }
            if (phone == null || "".equals(phone)) {
                error += "电话为空";
            }
            if (address == null || "".equals(address)) {
                error += "地址为空";
            }
            if (email == null || "".equals(email)) {
                error += "邮箱为空";
            }
            //先对数据简单的校验
            if (!"".equals(error)) {
                request.setAttribute("msg", "出错了！！<br/>" + error);
                request.getRequestDispatcher("/client/message.jsp").forward(request, response);
                return;
            }
            Customer customer = new Customer();
            customer.setUsername(username);
            customer.setPassword(password);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            //调用业务逻辑方法
            BusinessService businessService = new BusinessServiceImpl();
            String result = businessService.registerCustomer(customer);
            if (result == null) { // 注册成功，将页面重定向至登录主页
                request.setAttribute("mess", "注册成功，去激活邮件");
                request.getRequestDispatcher("/client/login.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("mess", "出错了！！<br/>" + result);
                request.getRequestDispatcher("/client/register.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
