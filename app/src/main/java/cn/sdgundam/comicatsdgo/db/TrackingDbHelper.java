package cn.sdgundam.comicatsdgo.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.NfcAdapter;

import cn.sdgundam.comicatsdgo.db.TrackingContract.Tracking;
/**
 * Created by xhguo on 12/15/2014.
 */
public class TrackingDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Tracking.db";

    public TrackingDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static final String SQL_CREATE =
            "CREATE TABLE " + Tracking.TABLE_NAME + " (" +
            Tracking.COLUMN_NAME_OBJECT_ID + " TEXT PRIMARY KEY, " +
            Tracking.COLUMN_NAME_OBJECT_TYPE + " TEXT PRIMARY KEY, " +
            Tracking.COLUMN_NAME_FIRST_VIEWED_AT + " INTEGER, " +
            Tracking.COLUMN_NAME_LAST_VIEWED_AT + " INTEGER, " +
            Tracking.COLUMN_NAME_VIEWS+ " INTEGER" + ")";
    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + Tracking.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }


}
