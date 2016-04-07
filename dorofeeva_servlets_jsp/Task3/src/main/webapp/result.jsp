<%@ page import="test.TaskClass" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Заголовок окна страницы результата</title>
    <link rel="stylesheet" type="text/css" href="theme.css">
</head>
<body>
<h1>Заголовок</h1>
<% TaskClass mt = ((TaskClass) session.getAttribute("mytask")); %>
Значения аргументов:<br>
Слово: <pre><%= mt.getWord() %><br></pre>
Количество повторений: <%= mt.getAmount() %><br>
Результат выполнения операции: <%= mt.getResult() %><br>
<a href="index.jsp">Ссылка на исходную страницу</a> <br>
<a href="result.xml">Ссылка на скачивание файла result.xml</a>
</body>
</html>