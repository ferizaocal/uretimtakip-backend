package com.productiontracking.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CronUtils {
    public static String convertDateToCronExpression(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ss mm HH dd MM ?");
        return dateFormat.format(date);
    }
}
