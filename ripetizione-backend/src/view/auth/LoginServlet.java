package view.auth;

import com.google.gson.Gson;
import dao.UserDao;
import excpetions.HttpException;
import model.User;
import utils.DatabaseUtil;
import utils.TokenUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns={"/api/login"})
public class LoginServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();

    }

    //Generate token and assign it to user
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String username=request.getParameter("username");
        String password=request.getParameter("password");
        PrintWriter out = response.getWriter();
        try {
            if(username==null || password==null || username.isEmpty() || password.isEmpty())
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Utente o password non fornite");

            User user = UserDao.getUser(username, password);
            String token = TokenUtil.generateToken("" + user.getId(),user.getUsername());
            user.setToken(token);
            UserDao.insertUserToken(user.getId(), user.getToken());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(user));
            out.flush();
        }
        catch (Exception e){
            throw new ServletException(e);
        }

    }
}