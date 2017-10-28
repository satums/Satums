
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    <%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>校验界面</title>
</head>

<body>
<table border="1px">
<tr>

<td>人员编号</td>
<td>姓名</td>
<td>科室编号</td>
</tr>

<tr>
<td>${user.code}</td>
<td>${user.username}</td>
<td>${user.departcode}</td>
</tr>

</table>
</body>
</html>