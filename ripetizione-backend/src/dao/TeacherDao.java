package dao;

import excpetions.HttpException;
import model.Teacher;
import utils.DatabaseUtil;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class TeacherDao {

    //Teacher List (Admin)
    public static ArrayList<Teacher> getTeachers() throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            ArrayList<Teacher> out=new ArrayList<>();
            s = c.prepareStatement("select docente.id, docente.nome, docente.cognome\n" +
                    "from docente\n" +
                    "where docente.deleted=FALSE\n");
            rs =s.executeQuery();
            while (rs.next()) {
                Teacher t = new Teacher(rs.getInt("id"),rs.getString("nome")+" "+rs.getString("cognome"));
                out.add(t);
            }
            return out;
        }finally {
            DatabaseUtil.closeAll(s,rs);
        }
    }

    public static ArrayList<Teacher> getTeachers(int idSubject) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            ArrayList<Teacher> out=new ArrayList<>();
            s = c.prepareStatement("select docente.id, docente.nome, docente.cognome\n" +
                    "from docente,corso_docente\n" +
                    "where docente.id=corso_docente.docente\n" +
                    "and docente.deleted=FALSE\n" +
                    "and corso_docente.corso=?\n");
            s.setInt(1,idSubject);
            rs =s.executeQuery();
            while (rs.next()) {
                Teacher t = new Teacher(rs.getInt("id"),rs.getString("nome")+" "+rs.getString("cognome"));
                out.add(t);
            }
            return out;
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
    }

    //Check createTeacherAndSLots()
    public static Teacher getTeacher(int id) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select docente.id, docente.nome, docente.cognome\n" +
                    "from docente\n" +
                    "where docente.id=?\n" +
                    "and docente.deleted=FALSE ");
            s.setInt(1,id);
            rs =s.executeQuery();
            if(rs.next()) {
                return new Teacher(rs.getInt("id"),rs.getString("nome")+" "+rs.getString("cognome"));
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Professore non trovato");

    }

    //Create a Teacher and his Slots (Admin)
    public static Teacher createTeacherAndSLots(String name,String surname) throws SQLException, NamingException {
        try(Connection c = DatabaseUtil.getConnection()){
            c.setAutoCommit(false);
            int teacherId = insertTeacher(c, name, surname);
            SlotDao.insertSlots(c, teacherId);
            c.commit();
            return getTeacher(teacherId);

        }

    }

    //Check createTeacherAndSlots()
    private static int insertTeacher(Connection c, String name,String surname) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try{
            String sql= "INSERT into docente values (DEFAULT ,? , ?,DEFAULT )";
            s = c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            s.setString(1,name);
            s.setString(2,surname);

            if(s.executeUpdate() > 0){
                rs = s.getGeneratedKeys();
                if(rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint")){
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Professore gia' inserito");
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }

        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del Server");

    }

    //Delete Teacher (Admin)
    public static void deleteTeacher(int id) throws SQLException, NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            s = c.prepareStatement("UPDATE docente set deleted=TRUE where id=?");
            s.setInt(1,id);
            result=s.executeUpdate();
            if(result>0)
                return;
        } finally {
            DatabaseUtil.closeAll(s,null);

        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Professore non trovato");
    }

    public static Teacher getTeacherFromSlot(int slot) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select docente.id, docente.nome, docente.cognome\n" +
                    "from docente,slot\n" +
                    "where docente.deleted=FALSE\n" +
                    "and slot.docente=docente.id\n" +
                    "and slot.id=?");
            s.setInt(1,slot);
            rs =s.executeQuery();
            if(rs.next()) {
                return new Teacher(rs.getInt("id"),rs.getString("nome")+" "+rs.getString("cognome"));
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Professore non trovato");
    }
}
