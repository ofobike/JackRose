package bookstore.web;

import bookstore.commons.Page;
import bookstore.domain.Book;
import bookstore.domain.Category;
import bookstore.domain.User;
import bookstore.service.BusinessService;
import bookstore.service.Impl.BusinessServiceImpl;
import bookstore.service.Impl.PrivilegeServciceImpl;
import bookstore.service.PrivilegeServcice;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理员操作的Servlet
 */
@WebServlet("/servlet/ManageServlet")
public class ManagerServlet extends BaseServlet {

    public void login(HttpServletRequest request,HttpServletResponse response){
        try {
            //获取参数
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            //调用业务层的方法
            PrivilegeServcice privilegeServcice = new PrivilegeServciceImpl();
            User user = privilegeServcice.login(username, password);
            if (user!=null){
                //用户登录成功
                request.getSession().setAttribute("user",user);
                //重定向到
                response.sendRedirect(request.getContextPath()+"/manage/index.jsp");
            }else {
                //用户登录失败
                request.setAttribute("msg","没有查询到数据!情确认数据");
                request.getRequestDispatcher("/login.jsp").forward(request,response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询数据库时出现异常!!!!");
        }

    }



    /**
     * 删除图书
     * @param request
     * @param response
     */
    public void deleteBook(HttpServletRequest request,HttpServletResponse response){
        try {
            String deleteBookId = request.getParameter("deleteBookId");
            BusinessService businessService = new BusinessServiceImpl();
            String result = businessService.deleteBookById(deleteBookId);
            if (result==null){
                //更新成功
                request.getRequestDispatcher("/servlet/ManageServlet?method=listBooks").forward(request,response);
            }else {
                //更新失败
                request.setAttribute("msg", "删除图书失败");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新图书
     * @param request
     * @param response
     */
    public void updateBookByAdmin(HttpServletRequest request,HttpServletResponse response){
        try {
            BusinessService businessService = new BusinessServiceImpl();
            Book book = uploadBook(request,businessService);
            System.out.println(book);
            //调用业务逻辑的方法
           String result =  businessService.updateBookByAdmin(book);
           if (result==null){
               //更新成功
                request.getRequestDispatcher("/servlet/ManageServlet?method=listBooks").forward(request,response);
           }else {
               //更新失败
               request.setAttribute("msg", "更新书籍书籍失败");
               request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 修改图书先查询图书
     * @param request
     * @param response
     */
    public void updateBookUI(HttpServletRequest request, HttpServletResponse response){
        //业务逻辑
        BusinessService businessService = new BusinessServiceImpl();
        try {
            //先查询所有的分类的再修改的页面的时候也可以选择分类
            List<Category> category = businessService.findAllCategory();
            String bookId = request.getParameter("bookId");
            Book book = businessService.findBookById(bookId);
            if (book!=null){
                //查询成功
                request.setAttribute("cc",category);
                request.setAttribute("book",book);
                //跳转到修改的页面
                request.getRequestDispatcher("/manage/updateBook.jsp").forward(request,response);
            }else {
                //没有查询到数据
                //添加书籍失败
                request.setAttribute("msg", "没有查询到书籍的数据");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 展示书籍
     * @param request
     * @param response
     */
    public void listBooks(HttpServletRequest request, HttpServletResponse response){
        try {
            BusinessService businessService = new BusinessServiceImpl();
            String num = request.getParameter("num");
            int pageNum = 0;
            if (num!=null && !"".equals(num) &&num.length()>0&&!"null".equals(num)){
                pageNum = Integer.parseInt(num);
            }else {
                pageNum=1;
            }
            Page page=businessService.findPage(pageNum);

            page.setUrl("/servlet/ManageServlet?method=listBooks");
            request.setAttribute("page", page);
            System.out.println(page);
            request.getRequestDispatcher("/manage/listBooks.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * 添加书籍
     *
     * @param request
     * @param response
     */
    public void addBook(HttpServletRequest request, HttpServletResponse response) {

        try {
            BusinessService businessService = new BusinessServiceImpl();
            Book book = uploadBook(request, businessService);
            //调用业务逻辑的处理方法
            String result = businessService.addBook(book);
            if (result==null){
                //添加成功
                request.setAttribute("msg", "添加书籍成功");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }else {
                //添加书籍失败
                request.setAttribute("msg", result);
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单独封装数据Book
     */
    protected Book uploadBook(HttpServletRequest request, BusinessService businessService) {

        //处理文件的上传
        Book book = new Book();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload load = new ServletFileUpload(factory);
            List<FileItem> list = load.parseRequest(request);
            for (int i = 0; i < list.size(); i++) {
                FileItem item = list.get(i);
                if (item.isFormField()) {
                    //获取属性
                    String fieldName = item.getFieldName();
                    //获取我们输入的值
                    String fieldValue = item.getString("utf-8");
                    //可以输出看看是什么
                    //System.out.println(fieldName+"--->"+fieldValue);
                    //利用BeanUtils来封装数据(获取的属性name 必须和传入的book属性一一对应)
                    BeanUtils.setProperty(book, fieldName, fieldValue);
                    //单独处理分类的id
                    if ("categoryId".equals(fieldName)) {
                        //调用业务逻辑
                        Category category = businessService.findCategoryById(fieldValue);
                        //添加book里面的属性
                        book.setCategory(category);
                    }
                } else {
                    //获取传入图片的所有名称
                    String file_name = item.getName();
                    //获取文件的扩展名(jpg npg)
                    String extension = FilenameUtils.getExtension(file_name);
                    file_name = UUID.randomUUID().toString()+"."+extension;
                    //把路径保存到book
                    book.setFilename(file_name);
                    //开始玩一些好玩的
                    //获取文件要保存的路径
                    String rootDirectory = this.getServletContext().getRealPath("/images");
                    //System.out.println("1-->"+rootDirectory);//D:\workhard\out\artifacts\day27_book_war_exploded\images
                    String childPath = getStoreDirecotry(rootDirectory);
                    //System.out.println("2-->"+childPath);
                    book.setPath(childPath);
                    //继续
                    String str = rootDirectory+File.separator+childPath+File.separator+file_name;
                    //System.out.println(str);//D:\workhard\out\artifacts\day27_book_war_exploded\images\2018-12-19\2fd7f4cd-8287-47a7-91d0-555655755321.jpg
                    //文件上传
                    item.write(new File(str));
                }
            }
        } catch (Exception e) {

        }
        return book;
    }
    private String getStoreDirecotry(String rootDirectory) {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String path = df.format(now);
        File file = new File(rootDirectory,path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }
    /**
     * 查询所有的分类
     */
    public void addBookUI(HttpServletRequest request, HttpServletResponse response) {
        try {
            //调用业务逻辑的方法
            BusinessService businessService = new BusinessServiceImpl();
            List<Category> list = businessService.findAllCategory();
            if (list == null) {
                //没有查询到
                //查询失败
                request.setAttribute("msg", "没有查询到分类数据");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            } else {
                //查询到数据
                request.setAttribute("cs", list);
                request.getRequestDispatcher("/manage/addBook.jsp").forward(request, response);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改分类
     *
     * @param request
     * @param response
     */
    public void updateCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Category category = new Category();
            BeanUtils.populate(category, parameterMap);
            //调用业务逻辑的方法
            BusinessService businessService = new BusinessServiceImpl();
            String result = businessService.updateCategory(category);
            if (result == null) {
                //修改成功
                request.getRequestDispatcher("/servlet/ManageServlet?method=listCategories").forward(request, response);
            } else {
                //修改失败
                request.setAttribute("msg", result);
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Id查询分类
     *
     * @param request
     * @param response
     */
    public void findAllCateByid(HttpServletRequest request, HttpServletResponse response) {
        try {
            String cid = request.getParameter("cid");
            BusinessService businessService = new BusinessServiceImpl();
            Category category = businessService.findCategoryById(cid);
            if (category == null) {
                //查询失败
                request.setAttribute("msg", "没有查询到数据");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            } else {
                //查询成功
                request.setAttribute("category", category);
                request.getRequestDispatcher("/manage/update.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除分类
     */
    public void deleteCate(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取参数
            String cid = request.getParameter("cid");
            //调用业务逻辑方法
            BusinessService businessService = new BusinessServiceImpl();
            String result = businessService.deleteCategoryById(cid);
            if (result == null) {
                //删除成功
                request.getRequestDispatcher("/manage/listCategories.jsp").forward(request, response);
            } else {
                //删除失败
                request.setAttribute("msg", result);
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看分类
     *
     * @param request
     * @param response
     */
    public void listCategories(HttpServletRequest request, HttpServletResponse response) {
        try {
            //不用接受参数
            BusinessService businessService = new BusinessServiceImpl();
            List<Category> list = businessService.findAllCategory();
            if (list == null) {
                //没有查询到数据
                request.setAttribute("msg", "没有查询到数据");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            } else {
                request.setAttribute("cs", list);
                request.getRequestDispatcher("/manage/listCategories.jsp").forward(request, response);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加分类
     *
     * @param request
     * @param response
     */
    public void addCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            String des = request.getParameter("des");
            //调用业务的逻辑的方法
            BusinessService businessService = new BusinessServiceImpl();
            String result = businessService.addCategory(name, des);
            if (result == null) {
                //添加成功
                request.setAttribute("msg", "添加分类成功");
                request.getRequestDispatcher("/manage/message.jsp").forward(request, response);
            } else {
                //添加失败
                request.setAttribute("error", result);
                request.getRequestDispatcher("/manage/addCategory.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检测书籍分类是否可以使用
     *
     * @param request
     * @param response
     */
    public void checkCategory(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            //获取参数
            String checkname = request.getParameter("checkname");
            if (checkname == null || "".equals(checkname)) {
                response.getWriter().print("<font color='pink'>没有输入数据</font>");
                return;
            }
            //调用业务层的方法
            BusinessService businessService = new BusinessServiceImpl();
            boolean exists = businessService.isCategoryExists(checkname);
            if (exists) {
                response.getWriter().print("<font color='red'>该分类已经存在</font>");
            } else {
                response.getWriter().print("<font color='green'>该分类可以使用</font>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
