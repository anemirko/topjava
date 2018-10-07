<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.getId()}">
    Дата: <input type="datetime-local" name="datetime" value="${meal.getDateTime()}"><br/>
    Описание:<input type="text" name="description" value="${meal.getDescription()}"><br/>
    Калории:<input type="number" name="calories" value="${meal.getCalories()}"><br/>
    <input type="submit" value="${meal.isNew()?'Добавить':'Сохранить'}">
    <form method="get" action="meals">
        <input type="reset" value="Сбросить"}>
    </form>
</form>
</body>
</html>