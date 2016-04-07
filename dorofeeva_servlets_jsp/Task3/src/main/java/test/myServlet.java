package test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/result.html")
public class myServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8"); //не сработало...
        req.setCharacterEncoding("UTF-8"); //не сработало...

        TaskClass mt = (TaskClass) req.getSession().getAttribute("mytask");
        if(mt==null)
        {
            resp.getWriter().print("<html><head><meta http-equiv=\"refresh\" content=\"5; url=/testweb\" /></head><body>Сообщение об ошибке<br><a href=\"/testweb\">Ввести исходные данные</a><body></html>");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mt.getAmount(); i++) {
            sb.append(mt.getWord());
            sb.append(" ");
        }
        mt.setResult(sb.toString());
        req.getRequestDispatcher("result.jsp").forward(req, resp);
    }
}
