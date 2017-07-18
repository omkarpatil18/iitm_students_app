package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.month_fragments.AprilFragment;
import in.ac.iitm.students.fragments.month_fragments.AugustFragment;
import in.ac.iitm.students.fragments.month_fragments.DecemberFragment;
import in.ac.iitm.students.fragments.month_fragments.FebruaryFragment;
import in.ac.iitm.students.fragments.month_fragments.JanuaryFragment;
import in.ac.iitm.students.fragments.month_fragments.JulyFragment;
import in.ac.iitm.students.fragments.month_fragments.JuneFragment;
import in.ac.iitm.students.fragments.month_fragments.MarchFragment;
import in.ac.iitm.students.fragments.month_fragments.MayFragment;
import in.ac.iitm.students.fragments.month_fragments.NovemberFragment;
import in.ac.iitm.students.fragments.month_fragments.OctoberFragment;
import in.ac.iitm.students.fragments.month_fragments.SeptemberFragment;

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
            return new JulyFragment();
        } else if (current == 1) {
            return new AugustFragment();
        } else if (current == 2) {
            return new SeptemberFragment();
        } else if (current == 3) {
            return new OctoberFragment();
        } else if (current == 4) {
            return new NovemberFragment();
        } else if (current == 5) {
            return new DecemberFragment();
        } else if (current == 6) {
            return new JanuaryFragment();
        } else if (current == 7) {
            return new FebruaryFragment();
        } else if (current == 8) {
            return new MarchFragment();
        } else if (current == 9) {
            return new AprilFragment();
        } else if (current == 10) {
            return new MayFragment();
        } else if (current == 11) {
            return new JuneFragment();
        } else return null;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
