package in.ac.iitm.students.organisations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.News;
import in.ac.iitm.students.organisations.activities.main.PostActivity;
import in.ac.iitm.students.organisations.adapters.NewsAdapter;

/**
 * Created by rohithram on 19/7/17.
 */

public class T5eFragment extends Fragment{

    public static TextView error_message;
    public PostActivity pactivity;
    public ArrayList<News> NewsList;
    public NewsAdapter adapter;
    ViewPager viewPager;
    RecyclerView mRecyclerView;




    public T5eFragment() {
        // Required empty public constructor
    }

    public void setResponse(ArrayList<News> NewsList, ViewPager viewPager) {
        this.viewPager = viewPager;
        this.NewsList = NewsList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_t5e
                , container, false);

        pactivity = (PostActivity) getActivity();

        error_message = (TextView) view.findViewById(R.id.tv_error_t5e);
        mRecyclerView = (RecyclerView)view. findViewById(R.id.recyclerfifthsetate);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(getContext(),NewsList);
        mRecyclerView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
