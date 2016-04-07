<%@ page import="test.TaskClass" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en" encoding="UTF-8">
<head>
    <meta charset="UTF-8">
    <title>Заголовок в браузере</title>
    <link rel="stylesheet" type="text/css" href="theme.css">
</head>
    <body>
        <h1>Заголовок 1</h1>
        <p>Сопроводительный текст</p>
        <form action="index.jsp">
            <%
                boolean emptyWord = false;
                String word = request.getParameter("word");
                if (word!=null && word.equals(""))
                    emptyWord = true;
            %>
            <span style=<%= !emptyWord?"color:black":"color:red"%>>Поле ввода 1:<% if (emptyWord) out.println(" необходимо ввести символы"); %><br></span>
            <input type="text" name="word" placeholder="Подсказка 1" style=<%= !emptyWord?"background-color: black":"outline-style: dashed; color: red"%>>
            <br>
            <%
                boolean correctAmount = true;
                String amount = request.getParameter("amount");
                if (amount!=null && (amount.equals("") || Integer.parseInt(amount)<0))
                    correctAmount = false;
            %>
            <span style=<%= correctAmount ?"color:black" : "color:red"%>>Поле ввода 2:<% if (!correctAmount) out.println(" введено некорректное количество"); %><br></span>
            <input type="text" name="amount" placeholder="Подсказка 2" style=<%= correctAmount?"background-color: black":"outline-style: dashed; color: red"%>>
            <br>
            <%
                if (word != null && amount != null && correctAmount && !emptyWord) {
                    session.setAttribute("mytask", new TaskClass(word, Integer.parseInt(amount)));
                    response.sendRedirect("result.html");
                }
            %>
            <input type="submit" value="Текст кнопки">
        </form>
    </body>
</html>