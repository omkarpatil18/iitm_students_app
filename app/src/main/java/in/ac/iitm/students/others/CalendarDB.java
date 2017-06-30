package in.ac.iitm.students.others;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SAM10795 on 28-06-2017.
 */

public class CalendarDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "calendar";
    public static final String COLUMN_EVENT_DATE = "event_date";
    public static final String COLUMN_EVENT_MONTH = "event_day";
    public static final String COLUMN_EVENT_DAY = "event_month";
    public static final String COLUMN_EVENT_DETAILS = "event_details";
    public static final String COLUMN_EVENT_REMIND = "event_remind";
    public static final String COLUMN_EVENT_HOLIDAY = "event_holiday";

    private static final String DATABASE_NAME = "calendar.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" +COLUMN_EVENT_DATE+ " integer, "
            +COLUMN_EVENT_MONTH + " integer, "
            +COLUMN_EVENT_DAY + " text not null, "
            +COLUMN_EVENT_DETAILS + " text not null, "
            +COLUMN_EVENT_HOLIDAY + " integer, "
            +COLUMN_EVENT_REMIND + " integer );";

    public CalendarDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("Database","Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.i("Database","Created2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CalendarDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
