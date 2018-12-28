<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/manage/header.jsp"%>
<h3>当前位置：添加新分类</h3>
<form action="${pageContext.request.contextPath}/servlet/ManageServlet?method=addCategory" method="post">
    <table border="1" width="438">
        <tr>
            <td>分类名称：</td>
            <td>
                <input name="name" id="name" /><span id="sname"></span>
            </td>
        </tr>
        <tr>
            <td>描述：</td>
            <td>
                <textarea rows="3" cols="38" name="des"></textarea>
            </td>
        </tr>
    </table>
    <input type="submit" value="添加"/>
</form>

<span style="color: hotpink">${error}</span>

</center>

<script type="text/javascript">
    //检测分类的名称是否可以使用
    $(function(){
        //绑定离开焦点事件
        $("#name").blur(function(){
            //获取输入框的值
           var name =  $(this).val();
           //发送异步请求
            $.get("/servlet/ManageServlet",{"method":"checkCategory","checkname":name},function(data){
                   $("#sname").html(data);
            },"text");
        });
    });
</script>
</body>
</html>