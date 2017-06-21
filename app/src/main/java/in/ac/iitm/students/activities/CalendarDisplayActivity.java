<<<<<<< HEAD
package in.ac.iitm.students.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;

import in.ac.iitm.students.R;
import in.ac.iitm.students.fragments.CalendarFragment;

/**
 * Created by admin on 14-12-2016.
 */

public class CalendarDisplayActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);
        String calendarUrl = getIntent().getStringExtra("calendar_url");
        //Toast.makeText(this, calendarUrl, Toast.LENGTH_SHORT).show();
        callFragment(calendarUrl);
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
||||||| merged common ancestors
=======
package in.ac.iitm.students.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.CalendarPagerAdapter;
import in.ac.iitm.students.fragments.CalendarFragment;

/**
 * Created by admin on 14-12-2016.
 */

public class CalendarDisplayActivity extends FragmentActivity {

    ViewPager viewPager;
    CalendarPagerAdapter calendarPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calendar);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        calendarPagerAdapter = new CalendarPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);

        //String calendarUrl = getIntent().getStringExtra("calendar_url");
        //Toast.makeText(this, calendarUrl, Toast.LENGTH_SHORT).show();
        //callFragment(calendarUrl);
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
>>>>>>> 1058df56dee3ae024f1d69a2eea1e9ddc51eab8c
