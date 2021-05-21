package view.courses;
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
import java.util.ArrayList;

import com.google.gson.Gson;
import dao.CourseDao;
import excpetions.HttpException;
import model.Course;
import utils.AuthUtils;
import utils.DatabaseUtil;

@WebServlet(name = "CoursesServlet", urlPatterns={"/api/courses"})
public class CoursesServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(PrintWriter out = response.getWriter()) {
            ArrayList<Course> courses = CourseDao.getAllCourses();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");

            out.println(new Gson().toJson(courses));
            out.flush();

        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String subject = req.getParameter("name");

        PrintWriter out = resp.getWriter();
        try{
            AuthUtils.authAdmin(token);
            if(subject == null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Name of subject not provided");

            Course result = CourseDao.insertCourse(subject);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");

            out.println(new Gson().toJson(result));
            out.flush();

        } catch (SQLException | NamingException e){
            throw new ServletException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token=req.getParameter("token");
        String id=req.getParameter("id");

        try{
            AuthUtils.authAdmin(token);
            if(id==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "ID not provided");

            CourseDao.deleteCourse(Integer.parseInt(id));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (SQLException | NamingException e){
            throw new ServletException(e);
        }
    }
}