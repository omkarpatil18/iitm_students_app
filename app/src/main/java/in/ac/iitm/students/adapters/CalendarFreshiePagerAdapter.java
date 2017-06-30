package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.BunkMonitorFragment;
import in.ac.iitm.students.fragments.CourseAddFragment;
import in.ac.iitm.students.fragments.TimeTableFragment;

/**
 * Created by SAM10795 on 27-06-2017.
 */

public class CalendarFreshiePagerAdapter extends FragmentPagerAdapter{
    private static int NUM_ITEMS = 2;

    public CalendarFreshiePagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new TimeTableFragment();
            case 1: return new BunkMonitorFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0: return "TimeTable";
            case 1: return "Bunk Monitor";
            default: return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
