package cn.sdgundam.comicatsdgo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhguo on 9/25/2014.
 */
public class Utility {
    public static boolean TryParseDate(String dateString, Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date = dateFormat.parse(dateString);
            return true;
        }
        catch(ParseException e) {
            return false;
        }
    }
}
