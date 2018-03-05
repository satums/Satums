<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片编辑</title>
</head>
<body>
<table border="1px">
<tr>
<td>序号</td>
<td>图片名称</td>
<td>类型</td>
<td>是否生效</td>
</tr>
<c:set var="a" value="0"></c:set>
<c:forEach items="${list}" var="maps" varStatus="status"> 

<tr>
 <input type="hidden" value="${maps.id}" id="${status.index+1}">
<td>${status.index+1}</td>
<td>${maps.names}</td>
<td>${maps.types}</td>
<td>
<select id="del" onChange="update${status.index+1}()">
<option value="${maps.is_del }">
<c:choose>  
  
   <c:when test="${maps.is_del=='2'}">    
   <c:out value="是"></c:out>
   </option>
     <option value ="1">否</option>
  
   </c:when>    
   <c:otherwise> 
  
     <c:out value="否"></c:out>
       </option>
     <option value ="2">是</option>
   </c:otherwise>  
</c:choose>  


</select>
</td>
<script>
function update${status.index+1}(){
	var is_del=document.getElementById("del").value;
	var id=document.getElementById("${status.index+1}").value;
	location.href="../user/updateBack?is_del="+is_del+"&id="+id;
}
</script>
</tr> 

</c:forEach>

</table>

</body>
</html>