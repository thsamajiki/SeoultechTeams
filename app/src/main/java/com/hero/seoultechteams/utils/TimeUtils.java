package com.hero.seoultechteams.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {    // 시간 관련 함수들을 클래스로 모아둠
    private static TimeUtils instance;

    private TimeUtils() {}

    public static TimeUtils getInstance() {
        if (instance == null) {
            instance = new TimeUtils();
        }
        return instance;
    }

    public String convertTimeFormat(long timestamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(timestamp);
        return dateFormat.format(date);
    }

    public String convertTimeFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
