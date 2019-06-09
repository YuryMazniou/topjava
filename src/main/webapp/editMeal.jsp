<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
    <style>
        .td{
            text-align: center;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<table border="5">
    <tr align="center">
        <td>Дата</td>
        <td>Название приема пищи</td>
        <td>Калории</td>
        <td>Действие</td>
    </tr>
    <c:forEach var="num" items="${list}">
        <tr align="center">
            <fmt:parseDate value="${num.dateTime}" pattern="yyyy-MM-dd" var="parsedDateTime" type="date" />
            <td><fmt:formatDate pattern="dd.MM.yyyy" value="${ parsedDateTime }" /></td>
            <td>${num.description}</td>
            <td>${num.calories}</td>
            <td><form method="get" action="CRUDmealsServlet"><input type="submit" value="Back"></form></td>
        </tr>
    </c:forEach>
    <tr>
        <form method="post" action="CRUDmealsServlet">
            <c:forEach var="num" items="${list}">
            <input type="hidden" name="ID" value="<c:out value="${num.id}"/>">
            <td align="center"><input type="datetime-local" name="date" required></td>
            <td align="center">
                <select name="nameMeal" required>
                <option value="Завтрак">Завтрак</option>
                <option value="Обед">Обед</option>
                <option value="Ужин">Ужин</option>
                </select>
            </td>
            <td><input type="text" name="calories" pattern="^[0-9]{1,3}$" required></td>
            <td><input type="submit" value="Enter"></td>
            </c:forEach>
        </form>
    </tr>
</table>
</body>
</html>
