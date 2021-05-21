package view.auth;

import dao.UserDao;
import model.User;
import utils.AuthUtils;
import utils.DatabaseUtil;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LogoutServlet", urlPatterns={"/api/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }
    //Unassigned token
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String token=request.getParameter("token");
        try{
            User user= AuthUtils.authUser(token);
            UserDao.removeUserToken(user.getToken());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        catch(SQLException | NamingException e){
            throw new ServletException(e);
        }


    }

}