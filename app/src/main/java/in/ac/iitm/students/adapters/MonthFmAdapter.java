package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.MonthFragment;

/**
 * Created by harshitha on 8/6/17.
 */

public class MonthFmAdapter extends FragmentPagerAdapter {


    public MonthFmAdapter(FragmentManager fm) {

        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        int current = position % 12;
        if (current == 0) {
            return new MonthFragment();
        } else return null;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
