package dao;

import java.sql.*;

import excpetions.HttpException;
import model.Slot;
import java.util.ArrayList;
import utils.DatabaseUtil;
import utils.DateUtil;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

public class SlotDao {

    //Slot Interface of Calendar (User and Admin)
    public static ArrayList<Slot> getAvailableSlots(int idProf, int idCourse) throws SQLException, NamingException{
        PreparedStatement s =null;
        ResultSet rs=null;
        try(Connection c = DatabaseUtil.getConnection()){
            ArrayList<Slot> out = new ArrayList<>();
            s = c.prepareStatement("select slot.id, slot.data, slot.ora_inizio\n" +
                    "from corso_docente,docente,corso,slot\n" +
                    "where docente.id=corso_docente.docente\n" +
                    "and corso.id=corso_docente.corso\n" +
                    "and slot.docente=docente.id\n" +
                    "and docente.deleted=FALSE\n" +
                    "and corso.deleted=FALSE\n" +
                    "and docente.id=?\n" +
                    "and corso.id=?\n " +
                    "and slot.data>=now() \n"+
                    "and slot.id not in(select slot from prenotazione where prenotazione.stato<>20)\n" +
                    "order by (titolo,docente.nome,slot.data,slot.ora_inizio)");
            s.setInt(1,idProf);
            s.setInt(2,idCourse);
            rs =s.executeQuery();
            while (rs.next()) {
                Slot slot = new Slot(rs.getInt("id"),rs.getString("data"), rs.getString("ora_inizio"));
                out.add(slot);
            }
            return out;
        } finally {
            DatabaseUtil.closeAll(s,rs);
        }
    }

    //Check insertSlots()
    private static void insertSlot(Connection c, int docente,String date,String time) throws SQLException, NamingException{
        PreparedStatement s =null;
        try {
            s = c.prepareStatement("INSERT into slot values (DEFAULT ,? , ?, ?)");
            s.setDate(1,Date.valueOf(date));
            s.setTime(2,Time.valueOf(time));
            s.setInt(3,docente);
            int result=s.executeUpdate();
            if(result>0)
                return;
        } finally {
            DatabaseUtil.closeAll(s,null);
        }
        throw new HttpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore da parte del server");
    }


    public static void insertSlots(Connection c, int docente) throws SQLException, NamingException{
        ArrayList<String> date= DateUtil.getWeekDate();
        for(String d : date){
            for(int i=0;i<DateUtil.times.length;i++) {
                insertSlot(c, docente, d, DateUtil.times[i]);
            }
        }
    }
}

