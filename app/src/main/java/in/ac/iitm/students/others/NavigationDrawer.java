package in.ac.iitm.students.others;

import android.content.Context;
import android.content.Intent;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.activities.main.ComplaintBoxActivity;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.activities.main.ImpContactsActivity;
import in.ac.iitm.students.activities.main.MapActivity;
import in.ac.iitm.students.activities.main.OrganisationsActivity;
import in.ac.iitm.students.activities.main.StudentSearchActivity;

/**
 * Created by harshitha on 22/6/17.
 */

public class NavigationDrawer {
    Intent intent;
    public boolean navActivity(int id, Context context,boolean f, Intent i) {
        intent = i;
        boolean flag=f;
        if (id == R.id.nav_home) {
            intent = new Intent(context, HomeActivity.class);
            flag = true;
        } else if (id == R.id.nav_search) {
            intent = new Intent(context, StudentSearchActivity.class);
            flag = true;
        } else if (id == R.id.nav_contacts) {
            intent = new Intent(context, ImpContactsActivity.class);
            flag = true;
        } else if (id == R.id.nav_map) {
            intent = new Intent(context, MapActivity.class);
            flag = true;
        } else if (id == R.id.nav_organisations) {
            intent = new Intent(context, OrganisationsActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_box) {
            intent = new Intent(context, ComplaintBoxActivity.class);
            flag = true;
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(context, CalendarActivity.class);
            flag = true;
        } else if (id == R.id.nav_subscriptions) {
            intent = new Intent(context, SubscriptionActivity.class);
            flag = true;

        } else if (id == R.id.nav_about) {
            intent = new Intent(context, AboutUsActivity.class);
            flag = true;

        }
        return flag;
    }
    public Intent getIntent(){
        return intent;
    }
}
