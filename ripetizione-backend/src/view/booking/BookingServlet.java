package view.booking;

import com.google.gson.Gson;
import dao.BookingDao;
import excpetions.HttpException;
import model.Booking;
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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "BookingServlet", urlPatterns={"/api/bookings"})
public class BookingServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DatabaseUtil.registerDriver();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String token=request.getParameter("token");
        PrintWriter out = response.getWriter();
        try{
            User user= AuthUtils.authUser(token);
            if(user.getIs_admin()){
                ArrayList<Booking> bookings=BookingDao.getBookings();
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                out.println(new Gson().toJson(bookings));
            }
            else{
                ArrayList<Booking> myBookings=BookingDao.getMyBookings(user.getId());
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                out.println(new Gson().toJson(myBookings));
            }
            out.flush();
        }
        catch (SQLException | NamingException e)  {
            throw new ServletException(e);
        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String token=request.getParameter("token");
        String course=request.getParameter("course");
        String slot=request.getParameter("slot");
        PrintWriter out = response.getWriter();
        try {
            User user= AuthUtils.authUser(token);
            if(course==null || slot==null)
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Course or slot not provided");

            Booking result=BookingDao.creteBooking(Integer.parseInt(course), Integer.parseInt(slot), user.getId());
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println(new Gson().toJson(result));
            out.flush();
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String token=request.getParameter("token");
        String id=request.getParameter("id");
        try {
            User user= AuthUtils.authUser(token);
            if (id == null ) {
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id booking not provided");
            }
            if (user.getIs_admin()) {
                BookingDao.deleteBooking(Integer.parseInt(id));
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            } else {
                BookingDao.deleteMyBooking(Integer.parseInt(id), user.getId());
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
            }
        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String token=request.getParameter("token");
        String id=request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            User user= AuthUtils.authUser(token);
            if (id == null ) {
                throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "Id booking not provided");
            }
            Booking toConfirm = BookingDao.confirmMyBooking(Integer.parseInt(id), user.getId());
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(toConfirm));
            out.flush();

        } catch (SQLException | NamingException e) {
            throw new ServletException(e);
        }

    }


}

