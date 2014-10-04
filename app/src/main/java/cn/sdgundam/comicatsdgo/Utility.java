package cn.sdgundam.comicatsdgo;

import android.content.Context;
import android.content.res.Resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhguo on 9/25/2014.
 */
public class Utility {
    public static Date parseDateSafe(String dateString) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        }
        catch(ParseException e) {
            return new Date();
        }
    }

    public static String getUnitImageURLByUnitId(String unitId) {
        return String.format("http://cdn.sdgundam.cn/data-source/acc/unit-3g/%s.png", unitId);
    }

    public static String dateStringByDay(Context ctx, Date date) {
        String result = "";

        Date now = new Date();
        float delta = (now.getTime() - date.getTime()) / 1000;

        if (delta < 86400) {
            result = ctx.getString(R.string.date_today);
        } else if (delta < (86400 * 2)) {
            result = ctx.getString(R.string.date_yesterday);
        } else if (delta< (86400 * 7)) {
            result = (int) Math.floor(delta / 86400.0) + ctx.getString(R.string.date_days_before);
        } else {
            result = SimpleDateFormat.getDateInstance().format(date);
        }

        return result;
    }
}
