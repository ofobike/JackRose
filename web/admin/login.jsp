<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/manage/header.jsp"%>
<center>
	<h1>欢迎登陆</h1>
	<form action="${pageContext.request.contextPath}/servlet/ManageServlet" method="post">
		<input type="hidden" name="method" value="login">
		<table border="1" bordercolor="gold" cellspacing="0" cellpadding="5" width="">
			<tr>
				<td>账号:</td>
				<td><input type="text" name="username"></td>
			</tr>
			<tr>
				<td>密码:</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td colspan=2 align="center">
					<input type="submit" value=" 登 陆 "/>&nbsp;
					<input type="reset" value=" 重 置 "/>
				</td>
			</tr>
		</table>
	</form>
	<br><br>
	<font color="red" size="5">${ msg }</font>
</center>
  </body>
</html>
