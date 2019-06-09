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
    <tr>
        <form method="post" action="CRUDmealsServlet">
            <td><input type="datetime-local" name="date" required></td>
            <td>
                <select name="nameMeal" required>
                    <option value="Завтрак">Завтрак</option>
                    <option value="Обед">Обед</option>
                    <option value="Ужин">Ужин</option>
                </select>
            </td>
            <td><input type="text" name="calories" required></td>
            <td><input type="submit" value="Enter"></td>
        </form>
    </tr>
</table>
</body>
</html>
