package view.auth;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.google.gson.Gson;
import dao.UserDao;
import excpetions.HttpException;
import utils.DatabaseUtil;
import model.User;

@WebServlet(name = "RegisterServlet", urlPatterns={"/api/register"})
public class RegisterServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        try {
            if(username==null || password==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Username o password non fornite");
            User toInsert=UserDao.registerUser(username, password);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println(new Gson().toJson(toInsert));
            out.flush();

        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }

}
