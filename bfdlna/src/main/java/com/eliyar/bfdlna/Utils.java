package com.eliyar.bfdlna;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by brikerman on 2017/5/14.
 */

public class Utils {
    public static Integer convertStringToSecond(String str_date) {
        String component[] = str_date.split(":");
        if (component.length == 3) {
            Integer h = Integer.parseInt(component[0]);
            Integer m = Integer.parseInt(component[1]);
            Integer s = Integer.parseInt(component[2]);
            return h * 3600 + m * 60 + s;
        } else {
            return 0;
        }
    }

    public static String convertSecondToString(Integer second) {
        Integer h = second / 3600;
        Integer m = (second % 3600) / 60;
        Integer s = second % 60;

        return String.format("%02d:%02d:%02d", h, m, s);
    }
}