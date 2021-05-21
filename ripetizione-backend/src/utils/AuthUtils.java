package utils;

import dao.UserDao;
import excpetions.HttpException;
import model.User;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class AuthUtils {

    public static User authUser(String token) throws SQLException, NamingException {
        User user= UserDao.getUserFromToken(token);
        if(user == null)
            throw new HttpException(HttpServletResponse.SC_UNAUTHORIZED, "Non autorizzato");
        return  user;
    }

    public static User authAdmin(String token) throws SQLException, NamingException {
        User user = authUser(token);
        if(!user.getIs_admin())
            throw new HttpException(HttpServletResponse.SC_FORBIDDEN, "Non permesso");
        return  user;
    }

}
