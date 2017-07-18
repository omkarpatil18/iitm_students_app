package in.ac.iitm.students.fragments.month_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.RecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AprilFragment extends Fragment {


    public AprilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_april, container, false);

        String[] april_day = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"};
        String[] april_date = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        String[] april_desc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a", "b", "c", "d"};
        List<String> april_day_list = new ArrayList(Arrays.asList(april_day));
        List<String> april_date_list = new ArrayList(Arrays.asList(april_date));
        List<String> april_desc_list = new ArrayList(Arrays.asList(april_desc));

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_april);
        rv.setHasFixedSize(true);
        RecyclerAdapter adapter = new RecyclerAdapter(april_day_list, april_date_list, april_desc_list, getActivity());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
