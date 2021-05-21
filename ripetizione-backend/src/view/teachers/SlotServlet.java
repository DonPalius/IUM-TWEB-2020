package view.teachers;

import com.google.gson.Gson;
import dao.SlotDao;
import excpetions.HttpException;
import model.Slot;
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


@WebServlet(name = "SlotServlet", urlPatterns={"/api/available_slots"})
public class SlotServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            String teacher=request.getParameter("teacher");
            String course=request.getParameter("course");
            if(teacher==null || course==null){
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST,"Teacher or course not provided");
            }

            ArrayList<Slot> calendar = SlotDao.getAvailableSlots(Integer.parseInt(teacher), Integer.parseInt(course));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.println(new Gson().toJson(calendar));
            out.flush();

        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }
}
