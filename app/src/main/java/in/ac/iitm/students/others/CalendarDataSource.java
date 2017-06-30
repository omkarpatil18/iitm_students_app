package in.ac.iitm.students.others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created by SAM10795 on 28-06-2017.
 */

public class CalendarDataSource {

    //Database fields
    private SQLiteDatabase database;
    private CalendarDB dbHelper;
    private Context context;
    private String[] allColumns = { CalendarDB.COLUMN_EVENT_DATE, CalendarDB.COLUMN_EVENT_MONTH,
            CalendarDB.COLUMN_EVENT_DAY, CalendarDB.COLUMN_EVENT_DETAILS,
            CalendarDB.COLUMN_EVENT_HOLIDAY, CalendarDB.COLUMN_EVENT_REMIND};

    public CalendarDataSource(Context context) {
        dbHelper = new CalendarDB(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertEvent(Calendar_Event event) {
        ContentValues values = new ContentValues();
        values.put(CalendarDB.COLUMN_EVENT_DATE, event.getDate());
        values.put(CalendarDB.COLUMN_EVENT_MONTH, event.getMonth());
        values.put(CalendarDB.COLUMN_EVENT_DAY, event.getDay());
        values.put(CalendarDB.COLUMN_EVENT_DETAILS, event.getDetails());
        values.put(CalendarDB.COLUMN_EVENT_HOLIDAY, event.isHoliday());
        values.put(CalendarDB.COLUMN_EVENT_REMIND, event.isRemind());
        long insertId = database.insert(CalendarDB.TABLE_NAME, null,
                values);
        //TODO: Verify
        /*Cursor cursor = database.query(CalendarDB.TABLE_NAME,
                allColumns, CalendarDB.COLUMN_EVENT_DATE + " = " + event.getDate()
                        + "AND" + CalendarDB.COLUMN_EVENT_MONTH + " = " + event.getMonth(), null,
                null, null, null);
        cursor.moveToFirst();
        Calendar_Event newEvent = cursorToEvent(cursor);
        cursor.close();*/
    }

    public void deleteEvent(Calendar_Event event) {
        Log.i("DBDelete",event.getDetails());
        database.delete(CalendarDB.TABLE_NAME, CalendarDB.COLUMN_EVENT_DATE
                + " = " + event.getDate() + "AND" + CalendarDB.COLUMN_EVENT_MONTH
                + " = " + event.getMonth() + "AND" + CalendarDB.COLUMN_EVENT_DETAILS
                + " = " + event.getDetails(), null);
    }

    public ArrayList<Calendar_Event> getAllEvents() {

        ArrayList<Calendar_Event> events = new ArrayList<>();

        Cursor cursor = database.query(CalendarDB.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Calendar_Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }

        cursor.close();
        return events;
    }

    private Calendar_Event cursorToEvent(Cursor cursor) {
        Calendar_Event event = new Calendar_Event();
        event.setDate(cursor.getInt(0));
        event.setMonth(cursor.getInt(1));
        event.setDay(cursor.getString(2));
        event.setDetails(cursor.getString(3));
        event.setHoliday(cursor.getInt(4)==1);
        event.setRemind(cursor.getInt(5)==1);
        return event;
    }

}
