package ru.firstset.whereisuser.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtils {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH);

    public static String getCurrentDateTimeString() {
        return sdf.format(new Date());
    }

}
