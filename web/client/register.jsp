<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/client/head.jsp" %>
<center>
    <h3>新用户注册</h3>
    <form action="${pageContext.request.contextPath}/servlet/ClientServlet?method=customerRegist" method="post">
        <table border="1" width="50%">
            <span>${error}</span>
            <tr>
                <td>用户名：</td>
                <td>
                    <input name="username" id="username"/><span style="color: pink" id="span"></span>
                </td>
            </tr>
            <tr>
                <td>密码：</td>
                <td>
                    <input type="password" name="password"/>
                </td>
            </tr>
            <tr>
                <td>电话：</td>
                <td>
                    <input name="phone"/>
                </td>
            </tr>
            <tr>
                <td>地址：</td>
                <td><input name="address"/></td>
            </tr>
            <tr>
                <td>邮箱：</td>
                <td>
                    <input name="email" id="email"/><span style="color: pink" id="spanEmail"></span>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="align-content: center">
                    <input type="submit" value="注册"/>
                </td>
            </tr>
        </table>
    </form>
</center>
<script>
    $(function () {
        //发送ajax判断用户是否可以使用
        $("#username").blur(function () {
            //使用离开焦点事件
            //获取输入框的值
            var username = $(this).val();
            //发送ajax请求
            $.get("/servlet/ClientServlet", {"method":"checkUsernmae","username":username}, function (data) {
                    $("#span").html(data);
            });
        });

        //发送ajax判断邮箱是否已经使用
        $("#email").blur(function () {
            var email = $(this).val();
            $.get("/servlet/ClientServlet",{"method":"checkEmal","email":email},function (date) {
                    $("#spanEmail").html(date);
            });
        });
    });
</script>
</body>
</html>

