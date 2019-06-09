<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="5">
    <tr align="center">
        <td>ID</td>
        <td>Дата</td>
        <td>Название приема пищи</td>
        <td>Калории</td>
    </tr>
<c:forEach var="num" items="${list}">
    <c:if test="${num.excess==true}">
        <tr align="center" bgcolor="red">
            <td>${num.id}</td>
            <fmt:parseDate value="${num.dateTime}" pattern="yyyy-MM-dd" var="parsedDateTime" type="date" />
            <td><fmt:formatDate pattern="dd.MM.yyyy" value="${ parsedDateTime }" /></td>
            <td>${num.description}</td>
            <td>${num.calories}</td>
        </tr>
    </c:if>
    <c:if test="${num.excess==false}">
        <tr align="center">
            <td>${num.id}</td>
            <fmt:parseDate value="${num.dateTime}" pattern="yyyy-MM-dd" var="parsedDateTime" type="date" />
            <td><fmt:formatDate pattern="dd.MM.yyyy" value="${ parsedDateTime }" /></td>
            <td>${num.description}</td>
            <td>${num.calories}</td>
        </tr>
    </c:if>
</c:forEach>
</table>
</body>
</html>
