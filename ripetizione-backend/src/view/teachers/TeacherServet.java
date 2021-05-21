package view.teachers;

import com.google.gson.Gson;
import dao.TeacherDao;
import excpetions.HttpException;
import model.Teacher;
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
import java.util.ArrayList;


@WebServlet(name = "TeacherServet", urlPatterns = {"/api/teachers"})
public class TeacherServet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String token = req.getParameter("token");
        PrintWriter out = resp.getWriter();
        try {
            AuthUtils.authAdmin(token);
            ArrayList<Teacher> result=TeacherDao.getTeachers();
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(result));
            out.flush();
        }
        catch (SQLException | NamingException e)  {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("application/json");

            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String token = request.getParameter("token");
            PrintWriter out = response.getWriter();
            try {
                AuthUtils.authAdmin(token);
                if (name == null || surname == null || token == null) {
                    throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Name Surname or token not provided");
                }
                Teacher result=TeacherDao.createTeacherAndSLots(name, surname);
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.println(new Gson().toJson(result));
                out.flush();
            }
            catch (SQLException | NamingException e)  {
                throw new ServletException(e);
            }


    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        DatabaseUtil.registerDriver();

        String id = request.getParameter("id");
        String token = request.getParameter("token");
        PrintWriter out = response.getWriter();
        try {
            AuthUtils.authAdmin(token);
            if (id == null ) {
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,"Id not provided");
            }
            TeacherDao.deleteTeacher(Integer.parseInt(id));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.flush();
        }
        catch (SQLException | NamingException e)  {
            throw new ServletException(e);
        }


    }
}
