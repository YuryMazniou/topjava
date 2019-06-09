<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
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
            <td><a href="CRUDmealsServlet">Back to List of Meals</a></td>
        </tr>
    </c:forEach>
    <tr>
        <form method="post" action="CRUDmealsServlet">
            <c:forEach var="num" items="${list}">
            <input type="hidden" name="ID" value="<c:out value="${num.id}"/>">
            <td><input type="datetime-local" name="date" required></td>
            <td><input type="text" name="nameMeal" pattern="[А-Яа-я]+" required></td>
            <td><input type="text" name="calories" pattern="^[0-9]+$" required></td>
            <td><input type="submit" value="Отправить"></td>
            </c:forEach>
        </form>
    </tr>
</table>
</body>
</html>
