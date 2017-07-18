package in.ac.iitm.students.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.ComplaintBoxActivity;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class MessAndFacilitiesActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_and_facilities);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.title_activity_mess_and_facilities);

        TextView header_name = (TextView) findViewById(R.id.header_name);

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

//        FirebaseInstanceId.getInstance().getToken();
//        FirebaseMessaging.getInstance().subscribeToTopic("test");
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase::", "Id" + firebaseToken);

        header_name.setText("Logged in as " + name);

        RelativeLayout complaint_thread = (RelativeLayout) findViewById(R.id.complaint_thread);
        complaint_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(MessAndFacilitiesActivity.this, MyComplaintsActivity.class);
                startActivity(in3);
            }
        });

    }

    public void onMessClick(View v) {
        changeActivity("Mess");
    }

    private void changeActivity(String type) {
        Intent i = new Intent(this, MessOrFacilitiesListActivity.class);
        i.putExtra("type", type);
        startActivity(i);
    }

    public void onFacilitiesClick(View v) {
        changeActivity("Facility");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessAndFacilitiesActivity.this, ComplaintBoxActivity.class);
        startActivity(intent);
    }
}
