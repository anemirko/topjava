<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        tr.red {
            color: red;
        }

        tr.green {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="1" cellspacing="0" bgcolor="#d3d3d3">
    <thead>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan="2">Действия</th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${meals}">
        <tr class="${meal.isExceed()?'red':'green'}">
            <fmt:parseDate value="${meal.getDateTime()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/></td>
            <td><c:out value="${meal.getDescription()}"/></td>
            <td><c:out value="${meal.getCalories()}"/></td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.getId()}" />">Редактировать</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.getId()}" />">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.getId()}">
    Дата:<input type="datetime-local"  name="datetime" value="${meal.getDateTime()}"><br/>
    Описание:<input type="text" name="description" value="${meal.getDescription()}"><br/>
    Калории:<input type="number" name="calories" value="${meal.getCalories()}"><br/>
    <input type="submit" value="${meal.isNew()?'Добавить':'Сохранить'}">
</form>
</body>
</html>