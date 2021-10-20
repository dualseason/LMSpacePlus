package edu.lingnan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Boolean isBetween2Date(String nowString, String startString, String endString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowTime = null;
        Date startTime = null;
        Date endTime = null;
        try {
            nowTime = sdf.parse(nowString);
            startTime = sdf.parse(startString);
            endTime = sdf.parse(endString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
