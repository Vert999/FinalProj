package com.example.finalproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaActivity extends AppCompatActivity {
    VideoView video;
    ExoPlayer exoP;
    TextView title;
    TextView description;
    int vidnum;
    Uri uri;
    String id;
    String api;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        video = findViewById(R.id.main_video);
        title = findViewById(R.id.titleView);
        MediaController mediaCont = new MediaController(this);
        mediaCont.setAnchorView(video);
        video.setMediaController(mediaCont);
        description = findViewById(R.id.description);
        api = sharedPref.getString("API", "");
        vidnum = sharedPref.getInt("video", 1);
        getAPI();
    }
    public void getAPI()// this lovely API call loops through the items in the api call and parses them out, then distributes the title and url (through the drawablefromurl method) into their respective Views
    {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray trendItems = response.optJSONArray("items");
                JSONObject lessItem = trendItems.optJSONObject(vidnum);
                JSONObject currentItem = lessItem.optJSONObject("snippet");
                Log.d("myapp", " The response is " + currentItem);
                try {
                    title.setText(currentItem.getString("title"));
                    description.setText(currentItem.getString("description"));
                    id = lessItem.getString("id");
                    uri = Uri.parse("https://www.youtube.com/watch?v=" + id);
                    video.setVideoURI(uri);
                    video.requestFocus();
                    video.start();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                editor.putString("search", s);
                editor.commit();
                startActivity(new Intent(MediaActivity.this, SearchActivity.class));
                return false;
            }// This Searchbar on the menu will send you to the SearchActivity with the search query following in the shared pref's

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.backItem:
                startActivity(new Intent(MediaActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
