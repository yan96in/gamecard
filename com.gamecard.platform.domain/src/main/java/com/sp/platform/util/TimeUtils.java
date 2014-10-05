package com.sp.platform.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String[] chuli(String kssj, String jssj) {
        String str[] = new String[2];
        if (StringUtils.isEmpty(kssj) || StringUtils.isEmpty(jssj)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            kssj = shj;
            jssj = shj;
        }
        if (kssj.length() == 10) {
            kssj = kssj + " 00:00:00";
        }
        if (jssj.length() == 10) {
            jssj = jssj + " 23:59:59";
        }

        str[0] = kssj;
        str[1] = jssj;
        return str;
    }
}
