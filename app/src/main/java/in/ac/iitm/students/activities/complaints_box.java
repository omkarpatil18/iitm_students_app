package in.ac.iitm.students.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.MessAndFacilitiesActivity;

public class complaints_box extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_box);

        // Find the View that shows the hostel complaints
        CardView hostel = (CardView) findViewById(R.id.cv_hostel);

        // Set a click listener on that View
        hostel.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the hostel cardView is clicked on.
            @Override
            public void onClick(View view) {
                Intent hostelIntent = new Intent(complaints_box.this, hostelComplaints.class);
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
                Intent mess_facIntent = new Intent(complaints_box.this, MessAndFacilitiesActivity.class);
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
                Intent generalIntent = new Intent(complaints_box.this, GeneralComplaints.class);
                startActivity(generalIntent);
            }
        });
    }

}
