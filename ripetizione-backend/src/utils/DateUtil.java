package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class DateUtil {
    public static String []times={
            "15:00:00",
            "16:00:00",
            "17:00:00",
            "18:00:00",
    };

    public static ArrayList<String> getWeekDate() {

        ArrayList<String> ret = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tmp = null;
        try {
            tmp = dateFormat.parse("2020-05-25");
        } catch (ParseException e) {
            tmp = new Date();
        }


        for(int i=0;i<5;i++) {
            ret.add(dateFormat.format(tmp));
            tmp.setTime(tmp.getTime() + 24*60*60*1000);
        }
        return ret;
    }

}
