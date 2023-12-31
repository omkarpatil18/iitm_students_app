package in.ac.iitm.students.organisations.request_classes;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.ac.iitm.students.organisations.activities.DeveloperKey;
import in.ac.iitm.students.organisations.activities.main.PostActivity;
import in.ac.iitm.students.organisations.object_items.VideoItem;

/**
 * Created by rohithram on 12/7/17.
 */

public class RequestVolley {

    Context context;
    public Void request(final String channelID, final Context context, final ArrayList<VideoItem> videoList, final Boolean isYoutube, final ViewPager viewPager, final TabLayout tabLayout) {

        this.context = context;

        String url = "https://www.googleapis.com/youtube/v3/search?key="+ DeveloperKey.DEVELOPER_KEY+"&channelId="+channelID+"&part=snippet,id&order=date&maxResults=50";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject videoIDJson = new JSONObject(response.toString());

                        JSONArray items = videoIDJson.getJSONArray("items");
                        for(int i=0;i<items.length();i++){
                            JSONObject item = items.getJSONObject(i);
                            JSONObject id = item.getJSONObject("id");

                            String kind = id.getString("kind");
                            if(kind.equals("youtube#playlist")){
                                // If want to add playlists ,then there some code to be added here.
                                continue;
                            }
                            else {
                                JSONObject snippet = item.getJSONObject("snippet");
                                String title= snippet.getString("title");
                                String videoId = id.getString("videoId");

                                VideoItem vi = new VideoItem();
                                vi.videoId = videoId;
                                vi.videoTitle = title;
                                vi.channelTitle = snippet.getString("channelTitle");
                                videoList.add(vi);

                            }
                        }
            /*The no of results per jsonObject is hardcoded to 50(it's the maximum we can get)
              so there is a "nextPagetoken" included if there are more results, this function(fetchdatafromNextPage()) takes care of them of them*/
                        if(videoIDJson.has("nextPageToken")) {
                            String nextpage = videoIDJson.getString("nextPageToken");
                            fetchDatafromNextPage(nextpage,channelID,videoList);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        PostActivity.pageadapter.notifyDataSetChanged();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                }
            });

        // Access the RequestQueue through your singleton class.
    MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
        return null;
    }

   private void fetchDatafromNextPage(String next, final String channelID, final ArrayList<VideoItem> videoList){

       String ytkey = DeveloperKey.DEVELOPER_KEY;

       String url ="https://www.googleapis.com/youtube/v3/search?key="+ytkey+"&channelId="+channelID+"&part=snippet,id&order=date&maxResults=50&pageToken=";

       JsonObjectRequest jsObjRequest = new JsonObjectRequest
               (Request.Method.GET,url+next, null, new Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {

                       try {
                           JSONObject videoIDJson = new JSONObject(response.toString());
                           JSONArray items = videoIDJson.getJSONArray("items");
                           for(int i=0;i<items.length();i++){
                               JSONObject item = items.getJSONObject(i);
                               JSONObject id = item.getJSONObject("id");
                               String kind = id.getString("kind");
                               if(kind.equals("youtube#playlist")){
                                   // If want to add playlists ,then there some code to be added here.

                                   continue;
                               }
                               else {
                                   JSONObject snippet = item.getJSONObject("snippet");
                                   String videoId = null;
                                   String title= snippet.getString("title");
                                   if(snippet.has("videoId")) {
                                       videoId = id.getString("videoId");
                                   }
                                   VideoItem vi = new VideoItem();
                                   vi.videoId = videoId;
                                   vi.videoTitle = title;
                                   videoList.add(vi);

                               }
                           }
                           if(videoIDJson.has("nextPageToken")) {
                               String nextpage = videoIDJson.getString("nextPageToken");
                               fetchDatafromNextPage(nextpage,channelID, videoList);
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // TODO Auto-generated method stub

                   }
               });

       // Access the RequestQueue through your singleton class.
       MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
   }
}
