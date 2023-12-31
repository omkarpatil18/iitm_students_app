package in.ac.iitm.students.organisations.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.organisations.adapters.VideoAdapter;
import in.ac.iitm.students.organisations.object_items.VideoItem;

/**
 * Created by rohithram on 13/7/17.
 */

public class YoutubeFragment extends Fragment {

    static VideoAdapter vadapter;
    ArrayList<VideoItem> response1;
    String channelID;
    ViewPager viewPager;
    RecyclerView recyclerView;


    public YoutubeFragment() {
        // Required empty public constructor
    }

    public void setResponse(ArrayList<VideoItem> response, String channelID, ViewPager viewPager) {
        this.channelID = channelID;
        this.response1 = response;
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.youtube_fragment
                , container, false);
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.videorv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        vadapter = new VideoAdapter(response1, context);
        recyclerView.setAdapter(vadapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vadapter = new VideoAdapter(response1,getContext());
        recyclerView.setAdapter(vadapter);
        vadapter.notifyDataSetChanged();
    }
}
