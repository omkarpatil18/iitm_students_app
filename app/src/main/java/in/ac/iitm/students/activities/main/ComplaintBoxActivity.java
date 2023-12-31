package in.ac.iitm.students.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.GeneralComplaints;
import in.ac.iitm.students.activities.HostelComplaints;
import in.ac.iitm.students.activities.MessAndFacilitiesActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.organisations.activities.main.Organizations;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class ComplaintBoxActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_box);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_complaint_box);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_complaint_box)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView rollNumber = (TextView) header.findViewById(R.id.tv_roll_number);

        username.setText(name);
        rollNumber.setText(roll_no);
        ImageView imageView = (ImageView) header.findViewById(R.id.user_pic);
        String urlPic = "https://photos.iitm.ac.in//byroll.php?roll=" + roll_no;
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

        // Find the View that shows the hostel complaints
        CardView hostel = (CardView) findViewById(R.id.cv_hostel);

        // Set a click listener on that View
        hostel.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the hostel cardView is clicked on.
            @Override
            public void onClick(View view) {
                Intent hostelIntent = new Intent(ComplaintBoxActivity.this, HostelComplaints.class);
                startActivity(hostelIntent);
            }
        });

        // Find the View that shows the mess and facilities complaints
        CardView mess_fac = (CardView) findViewById(R.id.cv_mess_fac);

        // Set a click listener on that View
        mess_fac.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the mess and facilities cardView is clicked on.
            @Override
            public void onClick(View view) {
                Intent mess_facIntent = new Intent(ComplaintBoxActivity.this, MessAndFacilitiesActivity.class);
                startActivity(mess_facIntent);
            }
        });

        // Find the View that shows the general complaints
        CardView general = (CardView) findViewById(R.id.cv_general);

        // Set a click listener on that View
        general.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the general cardView is clicked on.
            @Override
            public void onClick(View view) {
                Intent generalIntent = new Intent(ComplaintBoxActivity.this, GeneralComplaints.class);
                startActivity(generalIntent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        boolean flag = false;
        final Context context = ComplaintBoxActivity.this;

        if (id == R.id.nav_home) {
            intent = new Intent(context, HomeActivity.class);
            flag = true;
        } else if (id == R.id.nav_organisations) {
            intent = new Intent(context, Organizations.class);
            flag = true;
        } else if (id == R.id.nav_search) {
            intent = new Intent(context, StudentSearchActivity.class);
            flag = true;
        } else if (id == R.id.nav_map) {
            intent = new Intent(context, MapActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_box) {
            //intent = new Intent(context, ComplaintBoxActivity.class);
            //flag = true;
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(context, CalendarActivity.class);
            flag = true;
        } else if (id == R.id.nav_timetable) {
            intent = new Intent(context, TimetableActivity.class);
            flag = true;
        } else if (id == R.id.nav_contacts) {
            intent = new Intent(context, ImpContactsActivity.class);
            flag = true;
        } else if (id == R.id.nav_subscriptions) {
            intent = new Intent(context, SubscriptionActivity.class);
            flag = true;

        } else if (id == R.id.nav_about) {
            intent = new Intent(context, AboutUsActivity.class);
            flag = true;

        } else if (id == R.id.nav_log_out) {
            drawer.closeDrawer(GravityCompat.START);
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            LogOutAlertClass lg = new LogOutAlertClass();
                            lg.isSure(ComplaintBoxActivity.this);
                        }
                    }
                    , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
            );
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);

        //Wait till the nav drawer is closed and then start new activity (for smooth animations)
        Handler mHandler = new Handler();
        final boolean finalFlag = flag;
        final Intent finalIntent = intent;
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (finalFlag) {
                            context.startActivity(finalIntent);
                        }
                    }
                }
                , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
        );
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ComplaintBoxActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(ComplaintBoxActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(ComplaintBoxActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
