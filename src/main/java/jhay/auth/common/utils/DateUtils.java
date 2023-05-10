package jhay.auth.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final Integer expirationTime = 60*24;
    public static Date getExpirationDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
