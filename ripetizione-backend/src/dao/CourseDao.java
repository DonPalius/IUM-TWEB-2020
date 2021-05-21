package dao;

import excpetions.HttpException;
import model.Course;
import model.Teacher;
import utils.DatabaseUtil;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class CourseDao {

    //Courses Page (Only Admin)
    public static ArrayList<Course> getAllCourses() throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        ArrayList<Course> out = new ArrayList<>();
        try(Connection c = DatabaseUtil.getConnection()){
            s= c.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.deleted=FALSE");

            rs= s.executeQuery();

            //Teachers for every Course
            while (rs.next()) {
                ArrayList<Teacher> t = TeacherDao.getTeachers(rs.getInt("id"));
                Course course = new Course(rs.getInt("id"),rs.getString("titolo"), t);
                out.add(course);
            }

            return out;
        }finally {
            DatabaseUtil.closeAll(s,rs);
        }

    }

    //Insert Course (Only Admin)
    public static Course insertCourse(String subject) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){

            String sql = "INSERT into corso values (DEFAULT, ?, DEFAULT)";

            s= c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            s.setString(1,subject);

            if(s.executeUpdate() > 0){
                rs= s.getGeneratedKeys();
                if(rs.next())
                    return getCourse(rs.getInt(1));
            }

        } catch (SQLException e) {
            if(e.getMessage().contains("already exists"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Corso gia' esistente");
        }finally {
            DatabaseUtil.closeAll(s,rs);
        }

        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del Server");
    }

    //Return course after insertCourseToTeacher()
    private static Course getCourse(int id) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s= c.prepareStatement("select corso.id, corso.titolo " +
                    "from corso " +
                    "where corso.id=? " +
                    "and corso.deleted=FALSE ");
            s.setInt(1,id);
            rs=s.executeQuery();
            if(rs.next()){
                ArrayList<Teacher> t = TeacherDao.getTeachers(rs.getInt("id"));
                return new Course(rs.getInt("id"),rs.getString("titolo"), t);
            }
        }finally {
            DatabaseUtil.closeAll(s,rs);
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non trovato");
    }

    //Delete Course (Only Admin)
    public static void deleteCourse(int id) throws SQLException, NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s= c.prepareStatement("UPDATE corso set deleted=TRUE where id=? and deleted=FALSE");
            s.setInt(1,id);
            if(s.executeUpdate() > 0){
                return;
            }
        }finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso non trovato");
    }

    //Connect Teacher to Course (Only Admin)
    public static Course insertCourseToTeacher(int course, int teacher) throws SQLException, NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            String sql="INSERT into corso_docente values (DEFAULT ,?,?)";
            s= c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            s.setInt(1,course);
            s.setInt(2,teacher);
            result=s.executeUpdate();
            if(result>0){
                return getCourse(course);
            }

        } catch (SQLException e) {
            if(e.getMessage().contains("already exists")){
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Corso gia' associato a quel professore");
            }
        }finally {
            DatabaseUtil.closeAll(s, null);
        }

        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error");
    }

    public static void deleteCourseToTeacher(int course, int teacher) throws SQLException , NamingException{
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            String sql="DELETE from corso_docente where corso=? and docente=?";
            s= c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            s.setInt(1,course);
            s.setInt(2,teacher);
            result=s.executeUpdate();
            if(result>0){
                return;
            }
        } finally {
            DatabaseUtil.closeAll(s,null);
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Corso con quel professore non trovato");

    }
}
