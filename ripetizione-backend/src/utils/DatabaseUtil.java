package utils;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;

public class DatabaseUtil {

    public static Connection getConnection() throws NamingException, SQLException {
        return DriverManager.getConnection(DatabaseUtil.getUrl(),DatabaseUtil.getUser(),DatabaseUtil.getPassword());
    }

    public static String getUrl() throws NamingException {
        Context ctx = new InitialContext();
        Context env = (Context) ctx.lookup("java:comp/env");
        return (String) env.lookup("url");
    }

    public static String getUser() throws NamingException {
        Context ctx = new InitialContext();
        Context env = (Context) ctx.lookup("java:comp/env");
        return (String) env.lookup("user");
    }

    public static String getPassword() throws NamingException {
        Context ctx = new InitialContext();
        Context env = (Context) ctx.lookup("java:comp/env");
        return (String) env.lookup("password");
    }

    public static String getTime() throws NamingException {
        Context ctx = new InitialContext();
        Context env = (Context) ctx.lookup("java:comp/env");
        return (String) env.lookup("TTL");
    }
    

    public static void registerDriver() throws ServletException {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public static void closeAll(PreparedStatement s, ResultSet rs){
        if(s!=null){
            try{
                s.close();
            } catch (SQLException e){e.printStackTrace();}
        }
        if(rs!=null){
            try{
                rs.close();
            } catch (SQLException e){e.printStackTrace();}
        }
    }
}
