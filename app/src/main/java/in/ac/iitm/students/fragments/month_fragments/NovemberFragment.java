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


public class NovemberFragment extends Fragment {


    public NovemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_november, container, false);

        String[] nov_day = {"Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu"};
        String[] nov_date = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        String[] nov_desc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a", "b", "c", "d"};

        List<String> nov_day_list = new ArrayList(Arrays.asList(nov_day));
        List<String> nov_date_list = new ArrayList(Arrays.asList(nov_date));
        List<String> nov_desc_list = new ArrayList(Arrays.asList(nov_desc));

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_november);
        rv.setHasFixedSize(true);
        RecyclerAdapter adapter = new RecyclerAdapter(nov_day_list, nov_date_list, nov_desc_list, getActivity());
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
