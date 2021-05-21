package dao;

import java.sql.*;

import excpetions.HttpException;
import model.User;

import utils.DatabaseUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;


public class UserDao {

    //Login
    public static User getUser(String userName, String userPassword) throws SQLException,NamingException {
        PreparedStatement s=null;
        ResultSet rs =null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select * from utente where account= ? and password = ? ");
            s.setString(1, userName);
            s.setString(2, userPassword);
            rs = s.executeQuery();
            if(rs.next()) {
                return new User(rs.getString("account"), rs.getInt("id"), rs.getBoolean("isAdmin"), null);
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Utente o password errati");

    }

    //Register
    private static User getUser(int id) throws SQLException,NamingException {
        PreparedStatement s=null;
        ResultSet rs =null;
        User out=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select * from utente where id= ? ");
            s.setInt(1, id);
            rs = s.executeQuery();
            while (rs.next()) {
                out= new User(rs.getString("account"), rs.getInt("id"), rs.getBoolean("isAdmin"), null);
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
        return out;
    }

    public static User registerUser(String nomeUtente,String passwordField) throws SQLException,NamingException {
        PreparedStatement s=null;
        ResultSet rs =null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            s = c.prepareStatement("INSERT into utente values (DEFAULT ,? , ?,DEFAULT )",Statement.RETURN_GENERATED_KEYS);
            s.setString(1,nomeUtente);
            s.setString(2,passwordField);
            result=s.executeUpdate();

            if(result>0){
                rs = s.getGeneratedKeys();
                if(rs.next())
                    return getUser(rs.getInt(1));
            }
            s.close();
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Utente gia' esistente");
        }
        finally {
            DatabaseUtil.closeAll(s,rs);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");

    }

    //Insert Token into DB
    public static void insertUserToken(int id,String token) throws SQLException,NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            s = c.prepareStatement("INSERT into \"session_user\" values (? , ?,DEFAULT,DEFAULT )");
            s.setInt(1,id);
            s.setString(2,token);
            result=s.executeUpdate();
            if(result>0)
                return;

        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Token gia' esistente");
        }
        finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");
    }

    //Logout
    public static void removeUserToken(String token) throws SQLException,NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            s = c.prepareStatement("DELETE FROM \"public\".\"session_user\" WHERE \"token\" = ?");
            s.setString(1,token);
            result=s.executeUpdate();
            if(result>0)
                return;

        } finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");

    }
    //Authentication
    public static User getUserFromToken(String token) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs =null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select utente.*,\"session_user\".*\n" +
                    "from utente,\"session_user\"\n" +
                    "where utente.id=\"session_user\".user\n" +
                    "and \"session_user\".token=? ");
            s.setString(1,token);
            rs = s.executeQuery();
            if (rs.next()) {
                Date now=Calendar.getInstance().getTime();
                Date genereted=new java.util.Date(rs.getTimestamp("genereted").getTime());
                long from=(TimeUnit.MINUTES.convert(Math.abs(now.getTime()-genereted.getTime()),TimeUnit.MILLISECONDS));

                int ttl=Integer.parseInt(DatabaseUtil.getTime());
                if(ttl>from)
                    return new User(rs.getString("account"), rs.getInt("id"), rs.getBoolean("isAdmin"), rs.getString("token"));
                else
                    removeUserToken(token);
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
        return null;
    }
}
