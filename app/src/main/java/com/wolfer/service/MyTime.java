package com.wolfer.service;

import java.util.Calendar;

public class MyTime {
    private Calendar calendar = Calendar.getInstance();

    public int getHour() {
        String str[] = calendar.getTime().toString().substring(11, 16).split(":");
        return Integer.parseInt(str[0]);
    }

    public int getMinutes() {
        String str[] = calendar.getTime().toString().substring(11, 16).split(":");
        return Integer.parseInt(str[1]);
    }

    public String getWholeTime() {
        calendar = Calendar.getInstance();
        return calendar.getTime().toString().substring(11, 19);
    }

    public int getSecond() {
        return Integer.parseInt(calendar.getTime().toString().substring(17, 19));
    }

}
