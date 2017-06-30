package in.ac.iitm.students.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.games.event.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.CalendarPagerAdapter;
import in.ac.iitm.students.fragments.CalendarFragment;
import in.ac.iitm.students.objects.Calendar_Event;
import in.ac.iitm.students.others.CalendarDataSource;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.Utils;

/**
 * Created by admin on 14-12-2016.
 */

public class CalendarDisplayActivity extends FragmentActivity {

    ViewPager viewPager;
    CalendarPagerAdapter calendarPagerAdapter;
    long CalID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);

        String acc = "AccNameNew";
        String disp = "DispName";
        String inter = "IntName";

        CalID = Utils.getprefLong("CalID", this);
        if (CalID == -1) {
            CalID = insertCalendar(acc, inter, disp);
            Log.i("CalID", CalID + "");
            Utils.saveprefLong("CalID", CalID, this);
        }
        //Calendar Sync
        String url = "http://192.168.137.1/month.php";

        JsonArrayRequest jrequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //CalendarDataSource dataSource = new CalendarDataSource(CalendarDisplayActivity.this);
                //dataSource.open();

                try {
                    for (int i = 0; i < 30; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Calendar_Event event = new Calendar_Event();
                        event.setDate(jsonObject.getInt("date"));
                        event.setMonth(7);
                        event.setDay(jsonObject.getString("day"));
                        event.setDetails(jsonObject.getString("details"));
                        event.setHoliday(jsonObject.getInt("holiday") == 1);
                        event.setRemind(jsonObject.getInt("remind") == 1);
                        if (event.getDetails().length() > 0 && !exists(event)) {
                            insertEvents(CalID, event);
                        }
                        //dataSource.insertEvent(event);
                        Log.i("JSONResp3", event.getDay());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("month", "october");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(jrequest);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        calendarPagerAdapter = new CalendarPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);
        Utils.saveprefInt("TT_Screen", 0, this);

        //TODO: New adapter for freshies
        //String calendarUrl = getIntent().getStringExtra("calendar_url");
        //Toast.makeText(this, calendarUrl, Toast.LENGTH_SHORT).show();
        //callFragment(calendarUrl);
    }

    void insertEvents(long calId, Calendar_Event event) {

        Calendar cal = new GregorianCalendar(2017, event.getMonth()-1, event.getDate());    //Jan is 0
        cal.setTimeZone(TimeZone.getTimeZone("IST"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long start = cal.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.TITLE, event.getDetails());
        values.put(CalendarContract.Events.EVENT_LOCATION, "Chennai");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
        values.put(CalendarContract.Events.DESCRIPTION, event.isHoliday() ? "Holiday" : "");
// reasonable defaults exist:
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri =
                    getContentResolver().
                            insert(CalendarContract.Events.CONTENT_URI, values);
            long eventId = new Long(uri.getLastPathSegment());
            Log.i("EventID", eventId + "");
        }
    }

    boolean exists(Calendar_Event event) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            String[] proj =
                    new String[]{CalendarContract.Events.TITLE};
            Calendar cal = new GregorianCalendar(2017, event.getMonth(), event.getDate());
            cal.setTimeZone(TimeZone.getTimeZone("IST"));
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long start = cal.getTimeInMillis();

            Cursor cursor = getContentResolver().query(
                    CalendarContract.Events.CONTENT_URI,
                    proj,
                    CalendarContract.Events.TITLE + " = ? AND "+CalendarContract.Events.DTSTART+" = ?",
                    new String[]{event.getDetails(),Long.toString(start)},
                    null);
            return cursor.moveToFirst();
        }
        return false;
    }


    long getCalendarId(String acc)
    {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ?";
        String[] selectionArgs = new String[]{acc};
// Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            String[] EVENT_PROJECTION = new String[] {
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                    CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
            };

            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            if(cur.moveToFirst())
            {
                return cur.getLong(0);
            }
            Log.i("CurCount",cur.getCount()+"");
        }
        return -1;
    }

    long insertCalendar(String acc, String inter, String disp)
    {

        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, acc);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, inter);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, disp);
        //cv.put(CalendarContract.Calendars.CALENDAR_COLOR, yourColor);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, acc)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri result = this.getContentResolver().insert(calUri, cv);
        Log.i("Result",result.toString());
        return getCalendarId(acc);
    }

    public ViewPager returnpager()
    {
        return viewPager;
    }

    public CalendarPagerAdapter returnadapter()
    {
        return calendarPagerAdapter;
    }

    protected void callFragment(String url) {


        if (isOnline()) {
            FragmentManager transaction = getFragmentManager();
            transaction.beginTransaction()
                    .replace(R.id.fragment_container, CalendarFragment.newInstance(url))
                    .commit();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Internet Connection Failed", Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}