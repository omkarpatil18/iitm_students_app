package in.ac.iitm.students.fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.BunksAdapter;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class BunkMonitorFragment extends Fragment {

    GridView gridView;
    BunksAdapter bunksAdapter;
    ArrayList<Bunks> bunks;

    //TODO: Add reminders for slots

    public BunkMonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bunk_monitor, container, false);

        bunks = new ArrayList<>();

        getcourses();

        bunksAdapter = new BunksAdapter(getActivity(), bunks);
        gridView = (GridView)view.findViewById(R.id.bunk_list);

        gridView.setAdapter(bunksAdapter);
        gridView.setNumColumns((getActivity().getResources().getConfiguration().orientation
                ==Configuration.ORIENTATION_PORTRAIT)?2:3);  //3 if landscape
        
        return view;
    }


    public void getcourses()
    {
        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Bunks course = new Bunks();
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            course.setBunk_tot(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,getActivity()));
            course.setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()));
            bunks.add(course);
        }
    }


}
