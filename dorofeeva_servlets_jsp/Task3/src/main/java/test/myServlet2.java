package test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/result.xml")
public class myServlet2 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TaskClass mt = (TaskClass) req.getSession().getAttribute("mytask");
            resp.setContentType("text/xml");
            PrintWriter out = resp.getWriter();
            JAXBContext jaxbContext = JAXBContext.newInstance(TaskClass.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(mt, out);
        } catch (Exception e) {
            resp.getWriter().print("<html><body>"+e.getMessage()+"<body></html>");
        }
    }
}
