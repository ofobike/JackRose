<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/manage/header.jsp"%>
<style>
	.odd{
		background-color: #ffb8c6;
	}
	.even{
		background-color: #c0c0c0;
	}
</style>
<center>
<h3>当前位置：查询分类</h3>
    <c:if test="${empty cs}">
    	<h2>您还没有添加任何分类，<a href="${pageContext.request.contextPath}/manage/addCategory.jsp">添加</a></h2>
    </c:if>
    <c:if test="${!empty cs}">
    	<table border="1" width="70%">
    		<tr>
    			<th>选择</th>
    			<th>序号</th>
    			<th>分类名称</th>
    			<th>描述</th>
    			<th>操作</th>
    		</tr>
    		<c:forEach items="${cs}" var="c" varStatus="vs">
    			<tr class="${vs.index%2==0?'even':'odd'}">
	    			<td>
	    				<input type="checkbox" id="ids" value="${c.id}"/>
	    			</td>
	    			<td>${vs.count}</td>
	    			<td>${c.name}</td>
	    			<td>${c.des}</td>
	    			<td>
	    				[<a href="${pageContext.request.contextPath}/servlet/ManageServlet?method=findAllCateByid&cid=${c.id}">修改</a>]
	    				[<a href="${pageContext.request.contextPath}/servlet/ManageServlet?method=deleteCate&cid=${c.id}">删除</a>]
	    			</td>
	    		</tr>
    		</c:forEach>
    	</table>
    </c:if>
     </center>
  </body>
</html>
