<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/manage/header.jsp"%>
<h3>当前位置：添加新分类</h3>
<form action="${pageContext.request.contextPath}/servlet/ManageServlet?method=updateCategory" method="post">
    <input type="hidden" name="id" value="${category.id}">
    <table border="1" width="438">
        <tr>
            <td>分类名称：</td>
            <td>
                <input name="name" readonly="readonly" value="${category.name}"/>
            </td>
        </tr>
        <tr>
            <td>描述：</td>
            <td>
                <textarea rows="3" cols="38" name="des">
                    ${category.des}
                </textarea>
            </td>
        </tr>
    </table>
    <input type="submit" value="修改"/>
</form>

</center>
</body>
</html>
