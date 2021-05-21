package view;

import com.google.gson.Gson;
import excpetions.HttpException;
import model.ErrorMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name ="ErrorServlet", urlPatterns = {"/ErrorServlet"})
public class ErrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Exception e = (Exception) req.getAttribute("javax.servlet.error.exception");

        int code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = e.getMessage();

        if(e instanceof HttpException){
            HttpException m = (HttpException) e;
            code = m.getCode();
        }

        resp.setStatus(code);
        resp.getWriter().println(new Gson().toJson(new ErrorMessage(message)));
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


}
