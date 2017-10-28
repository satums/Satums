
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    <%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import = "java.util.*"%>
<%@ page import="cn.com.Data.Bo.AppBo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆界面</title>
</head>
<script>
function query(){
	var aa=document.getElementById("aa").innerHTML;
	var a=document.getElementById("username").value;
	var cc=document.getElementById("pwd").value;
	alert(cc);
	alert(a);
	
	window.location.href=aa+"/queryAction.do?codes="+a+"&pwd="+cc;
}
</script>
<body>
<%
AppBo bo=new AppBo();
String sql="select * from sh_user";
List list=new ArrayList();
list=bo.query(sql);
 System.out.println("=============="+list);
%>
<table border="1px">
<tr>
<td>编号</td>
<td>人员编号</td>
<td>姓名</td>
<td>密码</td>
<td>科室编号</td>
</tr>
<c:set var="a" value="0"></c:set>
<c:forEach items="${users}" var="user">
<tr>
<td>${a=a+1}</td>
<td>${user.code}</td>
<td>${user.username}</td>
<td>${user.pwd}</td>
<td>${user.departcode}</td>

</tr>
</c:forEach>
用户名：<input id="username"></input>
密码：<input id="pwd"></input>
<input type="submit" onclick="query()" value="提交"></input>
<tr style="display:none">
<td id="aa">${pageContext.request.contextPath}</td>
</tr>
</table>
</body>
</html>