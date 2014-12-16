package cn.sdgundam.comicatsdgo.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.Date;

/**
 * Created by xhguo on 12/15/2014.
 */
public class TrackingService {
    static final String TRACKING_TABLE_NAME = "tracking";

    static final String VIDEO_VIEWED_KEY = "VIDEO_VIEWED_%s";
    static final String UNIT_VIEWED_KEY = "UNIT_VIEWED_%s";

    Context context;

    public TrackingService(Context context) {
        this.context = context;
    }

    public void markVideoLastViewed(String videoPostId) {
        try {
            DB trackingDB = DBFactory.open(context, TRACKING_TABLE_NAME);
            trackingDB.put(String.format(VIDEO_VIEWED_KEY, videoPostId), new Date());
            trackingDB.close();
        } catch (SnappydbException ex) { }
    }

    public boolean isVideoViewed(String videoPostId) {
        try {
            DB trackingDB = DBFactory.open(context, TRACKING_TABLE_NAME);
            Date d = trackingDB.getObject(String.format(VIDEO_VIEWED_KEY, videoPostId), Date.class);
            trackingDB.close();

            return d != null;
        } catch (SnappydbException ex) {
            return false;
        }
    }

    public void markUnitViewed(String unitId) {
        try {
            DB trackingDB = DBFactory.open(context, TRACKING_TABLE_NAME);
            trackingDB.put(String.format(UNIT_VIEWED_KEY, unitId), new Date());
            trackingDB.close();
        } catch (SnappydbException ex) { }
    }

    public boolean isUnitViewed(String unitId) {
        try {
            DB trackingDB = DBFactory.open(context, TRACKING_TABLE_NAME);
            Date d = trackingDB.getObject(String.format(UNIT_VIEWED_KEY, unitId), Date.class);
            trackingDB.close();

            return d != null;
        } catch (SnappydbException ex) {
            return false;
        }
    }
}
