<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/client/head.jsp"%>
    <br/>
    商品分类：
    <c:forEach items="${cs}" var="c">
    	<a href="${pageContext.request.contextPath}/servlet/ClientServlet?method=listBookByCategory&categoryId=${c.id}">${c.name}</a>&nbsp;&nbsp;
    </c:forEach>
<hr>

    <hr/>
    <table>
    	<tr>
    		<c:forEach items="${page.records}" var="b">
    			<td>
    				<img src="${pageContext.request.contextPath}/images/${b.path}/${b.filename}" width="40%"/><br/>
    				书名：${b.name}<br/>
    				作者：${b.author}<br/>
    				售价：${b.price}<br/>
    				<a href="${pageContext.request.contextPath}/servlet/ClientServlet?method=buyBook&bookId=${b.id}">放入购物车</a>
    			</td>
    		</c:forEach>
    	</tr>	
    </table>	
    <%@ include file="/commons/page.jsp"%>
    
    </center>
  </body>
</html>
