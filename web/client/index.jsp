<%--
  Created by IntelliJ IDEA.
  User: ysj
  Date: 2018/12/27
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:forward  page="/servlet/ClientServlet" >
    <jsp:param value="listBooks" name="method" />
</jsp:forward>
</body>
</html>
