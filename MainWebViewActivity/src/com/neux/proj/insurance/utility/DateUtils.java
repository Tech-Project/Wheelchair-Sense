package com.neux.proj.insurance.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2013/12/31
 * Time: ¤U¤È 5:16
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {
    public static String getTodayString() {
        java.util.Date now = new GregorianCalendar().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(now);
    }

    public static String to14DateString(long time) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp now = new Timestamp(time);
        return df.format(now);
    }

    public static long to14TimeInMillis(String date) {

        if(date.length() != 14) return 0;

        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6)) - 1;
        int day = Integer.parseInt(date.substring(6,8));
        int hour = Integer.parseInt(date.substring(8,10));
        int min = Integer.parseInt(date.substring(10,12));
        int sec = Integer.parseInt(date.substring(12,14));

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hour, min,sec);
        return beginTime.getTimeInMillis();
    }
}
