package com.mengdd.utils;

import java.util.Date;

public class DataUtils {
    @SuppressWarnings("deprecation")
    public static String getCurrentTime() {

        Date date = new Date();

        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("create Time: " + date.toLocaleString());

        return sBuffer.toString();
    }

}
