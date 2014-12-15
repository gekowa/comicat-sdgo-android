package cn.sdgundam.comicatsdgo.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.sdgundam.comicatsdgo.db.TrackingContract;
import cn.sdgundam.comicatsdgo.db.TrackingDbHelper;

/**
 * Created by xhguo on 12/15/2014.
 */
public class TrackingService {
    Context context;

    public TrackingService(Context context) {
        this.context = context;
    }

    public void TrackObjectView(String objectType, String objectId) {
        TrackingDbHelper dbHelper = new TrackingDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TrackingContract.Tracking.COLUMN_NAME_OBJECT_TYPE, objectType);
        values.put(TrackingContract.Tracking.COLUMN_NAME_OBJECT_ID, objectId);

        db.insertWithOnConflict(TrackingContract.Tracking.TABLE_NAME, null, null, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
