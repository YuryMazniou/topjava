<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h1>List</h1>

<table border="5">
    <tr align="center">
        <td>ID</td>
        <td>Дата</td>
        <td>Название приема пищи</td>
        <td>Калории</td>
        <td>Редактирование</td>
        <td>Удаление</td>
    </tr>
    <c:forEach var="num" items="${list}">
        <tr align="center">
            <td>${num.id}</td>
            <fmt:parseDate value="${num.dateTime}" pattern="yyyy-MM-dd" var="parsedDateTime" type="date" />
            <td><fmt:formatDate pattern="dd.MM.yyyy" value="${ parsedDateTime }" /></td>
            <td>${num.description}</td>
            <td>${num.calories}</td>
            <td><a href="MealsServletRefactor?action=edit&userId=<c:out value="${num.id}"/>">Update</a></td>
            <td><a href="MealsServletRefactor?action=delete&userId=<c:out value="${num.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="MealsServletRefactor?action=insert">Add User</a></p>
</body>
</html>
