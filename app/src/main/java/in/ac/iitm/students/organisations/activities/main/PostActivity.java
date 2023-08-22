package in.ac.iitm.students.organisations.activities.main;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.News;
import in.ac.iitm.students.organisations.adapters.OrgPagerAdapter;
import in.ac.iitm.students.organisations.fragments.Fbfragment;
import in.ac.iitm.students.organisations.fragments.T5eFragment;
import in.ac.iitm.students.organisations.fragments.VideoFragment;
import in.ac.iitm.students.organisations.fragments.YoutubeFragment;
import in.ac.iitm.students.organisations.object_items.Posts;
import in.ac.iitm.students.organisations.object_items.VideoItem;
import in.ac.iitm.students.organisations.request_classes.GraphGetRequest;
import in.ac.iitm.students.organisations.request_classes.RequestVolley;
import in.ac.iitm.students.others.Utils;



public class PostActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener{

    public  static ArrayList<Posts> postList;
    public static Boolean isYoutube;
    public static String channelID;
    public static ArrayList<VideoItem> videoList;
    public static OrgPagerAdapter pageadapter;
    public static OrgPagerAdapter pageadapter1, pageadapter2;
    public static TabLayout tabLayout;
    public static ArrayList<News> newses;
    public static Boolean isT5e;
    static ViewPager viewPager;
    final Gson gson = new Gson();
    public  AccessToken key;
    public  FrameLayout layout_MainMenu;
    public PopupWindow reactions_popup,multipopup;
    public CardView  containerLayout;
    public RelativeLayout containerLayout2;
    public String logo_url;
    public String Pagename;
    public android.support.v4.app.FragmentManager fragmentManager;
    public VideoFragment fragment;
    public View layout1,layout;
    public ProgressDialog pd;
    public String NEWSSTRING = "fifthestatestring";
    public String TAG = "TheFifthEstateFragment";
    String pageid;
    String appid ;
    String reaction_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);

        appid = getString(R.string.facebook_app_id);
        reaction_url = getString(R.string.reaction_query);

        Intent i = getIntent();
        isYoutube = i.getBooleanExtra("isyoutube", false);
        if (isYoutube) {
            channelID = i.getStringExtra("ChannelId");
        }
        pageid = i.getStringExtra("pageid");
        Pagename = i.getStringExtra("pagename");
        logo_url = i.getStringExtra("logo_url");

        setTitle(Pagename);
        isT5e = false;

        containerLayout = (CardView) findViewById(R.id.cv_popup);
        reactions_popup = new PopupWindow(PostActivity.this);
        reactions_popup.setContentView(containerLayout);
        containerLayout2 = (RelativeLayout) findViewById(R.id.rl_multipopup);
        multipopup = new PopupWindow(PostActivity.this);
        multipopup.setContentView(containerLayout);
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) PostActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout = inflater.inflate(R.layout.react_popup, (ViewGroup) findViewById(R.id.cv_popup));
        layout1 = inflater.inflate(R.layout.multimagepopup, (ViewGroup) findViewById(R.id.rl_multipopup));

        pd = new ProgressDialog(PostActivity.this);
        pd.setMessage("Loading " + Pagename);
        pd.setCancelable(false);
        pd.show();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout



        fragmentManager = null;
        fragment = null;
        Bundle arg = new Bundle();
        arg.putString("intialvalue", "initialisable");

        View layout3 = inflater.inflate(R.layout.fb_fragment, (ViewGroup) findViewById(R.id.mainview));

        if (layout3.findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                    return;
                }
            // Create a new Fragment to be placed in the activity layout
            fragment = new VideoFragment();
            fragment.setArguments(arg);
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager = getSupportFragmentManager();
            }

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        postList = new ArrayList<>();
        videoList = new ArrayList<VideoItem>();
        newses = new ArrayList<News>();

        /* make the API call */

        key = Organizations.key;

        getdata();

        if(Pagename.equalsIgnoreCase("The Fifth Estate, IIT Madras")){
            isT5e = true;
            getNews();
        }
        callviewpager();

    }

    public void getdata(){

        if(isYoutube){
            RequestVolley obj = new RequestVolley();
            obj.request(channelID, PostActivity.this, videoList,isYoutube, viewPager, tabLayout);
        }
        GraphGetRequest request = new GraphGetRequest();
        try {
            request.dorequest(key, pageid + "/posts", null,postList,pd, reaction_url);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getNews() {
        //final ProgressDialog progress;

        final Context context1 = PostActivity.this;
        String url = getString(R.string.fifthettateurl);

        /*progress = //new ProgressDialog(context1);
        progress.setCancelable(false);
        progress.setMessage("Loading T5E...");
        progress.show();*/

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int id = 0;
                        Long dateINT = null;
                        String title = null, summary = null, content = null;
                        String imgurl = "";

                        for (int i = 0; response.length() > i; i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                id = jsonObject.getInt("id");
                                Date date = null;
                                SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy hh:mm a");
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("modified").replace("T", " "));
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                dateINT = date.getTime();
                                title = jsonObject.getJSONObject("title").getString("rendered");
                                summary = jsonObject.getJSONObject("excerpt").getString("rendered");
                                summary = Html.fromHtml(summary).toString();
                                content = jsonObject.getJSONObject("content").getString("rendered");
                                org.jsoup.nodes.Document doc = Jsoup.parse(content);
                                Elements castsImageUrl = doc.getElementsByTag("img");
                                if (castsImageUrl.size() > 0) {
                                    org.jsoup.nodes.Element el = castsImageUrl.get(0);
                                    imgurl = el.attr("src");
//                                    Log.d(TAG, imgurl);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {

                                newses.add(new News(id, title, summary, content, imgurl, dateINT));
                                pageadapter2.notifyDataSetChanged();

                            }
//                            Log.d(TAG, title);

                        }
                        String json = gson.toJson(newses);
                        Utils.saveprefString(NEWSSTRING, json, context1);
                       // mRecyclerView.setAdapter(new NewsAdapter(context1, newses));
                       //progress.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context1, (CharSequence) T5eFragment.error_message,Toast.LENGTH_SHORT);
                        String json = Utils.getprefString(NEWSSTRING, context1);

                        if (!json.equals("")) {
                            ArrayList<News> newses = gson.fromJson(json,
                                    new TypeToken<ArrayList<News>>() {
                                    }.getType());
                            //pageadapter2.notifyDataSetChanged();
                           // mRecyclerView.setAdapter(new NewsAdapter(context1, newses));
                        } else {
                            T5eFragment.error_message.setVisibility(View.VISIBLE);
                        }
                        //progress.dismiss();
                    }
                });
        in.ac.iitm.students.organisations.request_classes.MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public  void callviewpager(){
        if(videoList!=null && postList!=null && newses!=null ){
            if (isYoutube) {
                setupViewPager(viewPager);
                if (tabLayout != null) {
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
            else if(Pagename.equalsIgnoreCase("The Fifth Estate, IIT Madras")){
                setupViewPagerNoYoutubeWithT5E(viewPager);
                if (tabLayout != null) {
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
            else if(!isYoutube && !Pagename.equalsIgnoreCase("The Fifth Estate, IIT Madras")){
                tabLayout.setVisibility(View.INVISIBLE);
                tabLayout.getLayoutParams().width = 0;
                tabLayout.getLayoutParams().height = 0;
                setupViewPagerNoYoutube(viewPager);
            }

        }
    }


    public void setupViewPager(final ViewPager mviewPager) {

        pageadapter = new OrgPagerAdapter(getSupportFragmentManager());
        Fbfragment fbfragment = new Fbfragment();
        YoutubeFragment youtubeFragment = new YoutubeFragment();
        fbfragment.setResponse(postList,viewPager);
        youtubeFragment.setResponse(videoList,channelID,viewPager);
        pageadapter.addFragment(fbfragment,"Feed");
        pageadapter.addFragment(youtubeFragment,"Videos");
        mviewPager.setAdapter(pageadapter);
        pageadapter.notifyDataSetChanged();
    }

    public void setupViewPagerNoYoutube(ViewPager mviewPager) {

        pageadapter1 = new OrgPagerAdapter(getSupportFragmentManager());
        Fbfragment fbfragment = new Fbfragment();
        fbfragment.setResponse(postList, viewPager);
        pageadapter1.addFragment(fbfragment,"Feed");
        mviewPager.setAdapter(pageadapter1);
        pageadapter1.notifyDataSetChanged();
    }

    public void setupViewPagerNoYoutubeWithT5E(ViewPager mviewPager) {

        pageadapter2 = new OrgPagerAdapter(getSupportFragmentManager());
        Fbfragment fbfragment = new Fbfragment();
        T5eFragment t5eFragment = new T5eFragment();
        t5eFragment.setResponse(newses,viewPager);
        fbfragment.setResponse(postList, viewPager);
        pageadapter2.addFragment(fbfragment,"Feed");
        pageadapter2.addFragment(t5eFragment,"News");
        mviewPager.setAdapter(pageadapter2);
        pageadapter2.notifyDataSetChanged();

    }

    public void dim(){
        //layout_MainMenu.getForeground().setAlpha(160);
    }


    public void normal(){
      //  layout_MainMenu.getForeground().setAlpha(0);
    }
}




