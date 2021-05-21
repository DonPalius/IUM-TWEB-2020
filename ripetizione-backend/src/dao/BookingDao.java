package dao;

import excpetions.HttpException;
import model.Booking;
import utils.DatabaseUtil;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class BookingDao {

    //User Bookings interface
    public static ArrayList<Booking> getMyBookings(int idUser) throws SQLException, NamingException {
        ArrayList<Booking> out = new ArrayList<>();
        PreparedStatement s=null;
        ResultSet rs=null;
        try (Connection c = DatabaseUtil.getConnection()) {
             s = c.prepareStatement("select prenotazione.id,corso.titolo,docente.nome,docente.cognome,utente.account,prenotazione.stato,slot.data,slot.ora_inizio\n" +
                    "from prenotazione, corso,docente,utente,slot\n" +
                    "where prenotazione.utente=utente.id\n" +
                    "and prenotazione.corso=corso.id\n" +
                    "and prenotazione.slot=slot.id\n" +
                    "and slot.docente=docente.id\n" +
                    "and utente.id=?\n"+
                    "order by prenotazione.id desc");
            s.setInt(1,idUser);
            rs=s.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(rs.getInt("id"),rs.getString("titolo"), rs.getString("nome")+" "+rs.getString("cognome"),rs.getString("account"),rs.getInt("stato"),rs.getString("data"),rs.getString("ora_inizio"));
                out.add(booking);
            }

            return out;

        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
    }

    //"Prenota"
    public static Booking creteBooking(int course,int slot,int user) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            int result=0;
            SlotDao.getAvailableSlots(TeacherDao.getTeacherFromSlot(slot).getId(),course);
            s = c.prepareStatement("INSERT into prenotazione values (DEFAULT ,? , ?,?,? )",Statement.RETURN_GENERATED_KEYS);
            s.setInt(1,user);
            s.setInt(2,course);
            s.setInt(3,slot);
            s.setInt(4,10);
            result=s.executeUpdate();

            if(result>0){
                rs = s.getGeneratedKeys();
                if(rs.next())
                    return getBooking(rs.getInt(1));
            }
            s.close();
        } catch (SQLException e) {
            if(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Prenotazione gia' esistente");
            if(e.getMessage().contains("violates foreign key constraint"))
                throw new HttpException(HttpServletResponse.SC_CONFLICT, "Corso o Slot non trovato");
        }finally {
            DatabaseUtil.closeAll(s,rs);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");
    }

    //Booking you get with createBooking() and confirmMyBooking()
    private static Booking getBooking(int id) throws SQLException , NamingException{
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select prenotazione.id,corso.titolo,docente.nome,docente.cognome,utente.account,prenotazione.stato,slot.data,slot.ora_inizio\n" +
                    "from prenotazione, corso,docente,utente,slot\n" +
                    "where prenotazione.utente=utente.id\n" +
                    "and prenotazione.corso=corso.id\n" +
                    "and prenotazione.slot=slot.id\n" +
                    "and slot.docente=docente.id\n" +
                    "and prenotazione.id=?");
            s.setInt(1,id);
            rs=s.executeQuery();
            if(rs.next()) {
                return new Booking(rs.getInt("id"),rs.getString("titolo"), rs.getString("nome")+" "+rs.getString("cognome"),rs.getString("account"),rs.getInt("stato"),rs.getString("data"),rs.getString("ora_inizio"));
            }
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");

    }

    //Delete Button for Admin
    public static void deleteBooking(int id) throws SQLException, NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("update prenotazione set stato=20 where id=?");
            s.setInt(1,id);
            int result=s.executeUpdate();
            if(result>0){
                return;
            }
            s.close();
        } finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");

    }

    //Delete Button for User
    public static void deleteMyBooking(int id,int user) throws SQLException, NamingException {
        PreparedStatement s=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("update prenotazione set stato=20 where id=? and utente=?");
            s.setInt(1,id);
            s.setInt(2,user);
            int result=s.executeUpdate();
            if(result>0){
                return;
            }
            s.close();
        } finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");
    }

    //Confirm Button for User and Admin
    public static Booking confirmMyBooking(int id,int user) throws SQLException, NamingException {
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("update prenotazione set stato=30 where id=? and utente=? and stato=10",Statement.RETURN_GENERATED_KEYS);
            s.setInt(1,id);
            s.setInt(2,user);
            int result=s.executeUpdate();

            if(result>0){
                rs = s.getGeneratedKeys();
                if(rs.next())
                    return getBooking(rs.getInt(1));
            }
            s.close();
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
        throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Prenotazione non trovata");
    }

    //Admin Bookings interface
    public static ArrayList<Booking> getBookings() throws SQLException, NamingException {

        ArrayList<Booking> out = new ArrayList<>();
        PreparedStatement s=null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            s = c.prepareStatement("select prenotazione.id,corso.titolo,docente.nome,docente.cognome,utente.account,prenotazione.stato,slot.data,slot.ora_inizio\n" +
                    "from prenotazione, corso,docente,utente,slot\n" +
                    "where prenotazione.utente=utente.id\n" +
                    "and prenotazione.corso=corso.id\n" +
                    "and prenotazione.slot=slot.id\n" +
                    "and slot.docente=docente.id\n" +
                    "order by prenotazione.id desc");
            rs=s.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking(rs.getInt("id"),rs.getString("titolo"), rs.getString("nome")+" "+rs.getString("cognome"),rs.getString("account"),rs.getInt("stato"),rs.getString("data"),rs.getString("ora_inizio"));
                out.add(booking);
            }

            return out;

        } finally {
            DatabaseUtil.closeAll(s,rs);
        }

    }
}
