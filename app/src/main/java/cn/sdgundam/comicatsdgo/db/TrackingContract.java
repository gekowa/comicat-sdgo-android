package cn.sdgundam.comicatsdgo.db;

import android.provider.BaseColumns;

/**
 * Created by xhguo on 12/15/2014.
 */
public final class TrackingContract {
    public TrackingContract() {}

    public static abstract class Tracking implements BaseColumns {
        public static final String TABLE_NAME = "Tracking";

        public static final String COLUMN_NAME_OBJECT_TYPE = "ObjectType";
        public static final String COLUMN_NAME_OBJECT_ID = "ObjectId";
        public static final String COLUMN_NAME_FIRST_VIEWED_AT = "FirstViewedAt";
        public static final String COLUMN_NAME_LAST_VIEWED_AT = "LastViewedAt";
        public static final String COLUMN_NAME_VIEWS = "Views";

    }
}
