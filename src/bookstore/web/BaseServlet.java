package bookstore.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {
        public void service(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            req.setCharacterEncoding("utf-8");
            resp.setContentType("text/html;charset=utf-8");
            /*
             * 1. 获取参数，用来识别用户想请求的方法
             * 2. 然后判断是否哪一个方法，是哪一个我们就调用哪一个
             */
            String methodName = req.getParameter("method");

            if (methodName == null || methodName.trim().isEmpty()) {
                throw new RuntimeException("您没有传递method参数！无法确定您想要调用的方法！");
            }

            /*
             * 得到方法名称，是否可通过反射来调用方法？
             * 1. 得到方法名，通过方法名再得到Method类的对象！
             *   * 需要得到Class，然后调用它的方法进行查询！得到Method
             *   * 我们要查询的是当前类的方法，所以我们需要得到当前类的Class
             */
            Class c = this.getClass();//得到当前类的class对象
            Method method = null;
            try {
                method = c.getMethod(methodName,
                        HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(this, req, resp);
            }  catch (Exception e) {
                System.out.println("您调用的方法：" + methodName + ",　它内部抛出了异常！");
                throw new RuntimeException(e);
            }
        }
    }
