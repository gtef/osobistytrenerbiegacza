package com.example.jakub.osobistytrenerbiegacza.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jakub on 2016-03-07.
 */
public class DateFormatter {
    public static final String STANDARD_FORMAT = "dd.MM.yyyy";

    public static String toString(Date date, String format){
        SimpleDateFormat dt1 = new SimpleDateFormat(format);
        return dt1.format(date);
    }

    /**
     *
     * @param velocity m/s
     * @return
     */
    public static String minPerKm(double velocity){
        if(velocity == 0.0) {
            return "0'0\"/km";
        }
        velocity = 1.0 / velocity * 1000.0;

        int minutes = (int) (velocity / 60);
        int seconds = (int) (velocity % 60);
        String format = seconds >= 10 ? "%d'%d\"/km": "%d'0%d\"/km";
        return String.format(format, minutes, seconds);
    }

    /**
     *
     * @param velocity s/km
     * @return
     */
    public static String minPerKm2(double velocity){
        int minutes = (int) (velocity / 60);
        int seconds = (int) (velocity % 60);
        String format = seconds >= 10 ? "%d'%d\"/km": "%d'0%d\"/km";
        return String.format(format, minutes, seconds);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     *
     * @param seconds in seconds
     * @return
     */
    public static String time(int seconds) {
        String ret;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        if (minutes == 0) {
            ret = String.format("%02d:%02d", minutes, seconds);
        } else {
            minutes = minutes % 60;
            ret = String.format("%02d.%02d:%02d", hours, minutes, seconds);
        }
        return ret;
    }
}
