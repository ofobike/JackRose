<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Welcome</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <script src="../js/jquery-1.11.3.js"></script>
</head>
<body>
<center>
    <br><br>
    <h1>欢迎光临</h1>
    <br>
    <a href="${pageContext.request.contextPath}/client/head.jsp">首页</a>
    <c:if test="${sessionScope.customer==null}">
        <a href="${pageContext.request.contextPath}/client/login.jsp">登录</a>
        <a href="${pageContext.request.contextPath}/client/register.jsp">注册</a>
    </c:if>
    <c:if test="${sessionScope.customer!=null}">
        <span style="color: deeppink">${sessionScope.customer.username}</span>
        <a href="${pageContext.request.contextPath}/servlet/ClientServlet?method=logout">注销</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/servlet/ClientServlet?method=showOrders">我的订单</a>
    <a href="${pageContext.request.contextPath}/client/showCart.jsp">我的购物车</a>
</center>

