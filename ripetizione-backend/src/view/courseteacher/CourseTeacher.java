package view.courseteacher;

import com.google.gson.Gson;
import dao.CourseDao;
import excpetions.HttpException;
import model.Course;
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
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "CourseTeacher", urlPatterns={"/api/course_teacher"})
public class CourseTeacher extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String course=request.getParameter("course");
        String teacher=request.getParameter("teacher");
        String token=request.getParameter("token");
        PrintWriter out = response.getWriter();
        try{
            AuthUtils.authAdmin(token);
            if(course==null || teacher==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Teacher or Course not provided");

            Course result= CourseDao.insertCourseToTeacher(Integer.parseInt(course),Integer.parseInt(teacher));
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println(new Gson().toJson(result));
            out.flush();
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String course=req.getParameter("course");
        String teacher=req.getParameter("teacher");
        String token=req.getParameter("token");
        try{
            AuthUtils.authAdmin(token);
            if(course==null || teacher==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Teacher or Course not provided");

            CourseDao.deleteCourseToTeacher(Integer.parseInt(course),Integer.parseInt(teacher));
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }
}
