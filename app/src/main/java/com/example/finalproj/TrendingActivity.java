package com.example.finalproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TrendingActivity extends AppCompatActivity {
    CardView trending_one;
    CardView trending_two;
    CardView trending_three;
    CardView trending_four;
    CardView trending_five;
    CardView trending_six;
    CardView trending_seven;
    CardView trending_eight;
    TextView video_name1;
    TextView video_name2;
    TextView video_name3;
    TextView video_name4;
    TextView video_name5;
    TextView video_name6;
    TextView video_name7;
    TextView video_name8;
    ImageView thumbnail_1;
    ImageView thumbnail_2;
    ImageView thumbnail_3;
    ImageView thumbnail_4;
    ImageView thumbnail_5;
    ImageView thumbnail_6;
    ImageView thumbnail_7;
    ImageView thumbnail_8;

    String thumb;
    String title;
    int current;
    Drawable re;
    String API;
    int sharedVid;
    ArrayList<TextView> titleNames = new ArrayList<>();
    ArrayList<ImageView> thumbnails = new ArrayList<>();
    ArrayList<Integer> draws = new ArrayList<>();
    ArrayList<CardView> cards = new ArrayList<>();
    // I set up these ArrayLists to more efficiently go through the different view's
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        getItems();
        getAPI();
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        for (int j = 0; j < 8; j++) // This sets up an onclick listener for each card I have, with as minimal as code as I could fit
        {
            sharedVid = j+1;
            cards.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("API", API);
                    editor.commit();
                    editor.putInt("video", sharedVid);
                    editor.commit(); // whenever you onto a card video it will send the api call and position in the items so it can be called in the mediaplayer
                    startActivity(new Intent(TrendingActivity.this, MediaActivity.class));
                }
            });
        }
    }
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
                startActivity(new Intent(TrendingActivity.this, SearchActivity.class));
                return false;
                // This Searchbar on the menu will send you to the SearchActivity with the search query following in the shared pref's
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.backItem:
                startActivity(new Intent(TrendingActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getAPI() // this lovely API call loops through the items in the api call and parses them out, then distributes the title and url (through the drawablefromurl method) into their respective Views
    {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        API = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=US&key=AIzaSyDNw6r-xqZOS-WolHQqrP0PGSoYDBcmg_0&maxResults=9";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                API, null, new Response.Listener<JSONObject>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(JSONObject response) {
                JSONArray trendItems = response.optJSONArray("items");
                for (int i = 1; i < trendItems.length(); i++)
                {
                    JSONObject lessItem = trendItems.optJSONObject(i);
                    JSONObject currentItem = lessItem.optJSONObject("snippet");
                    JSONObject thumbing = currentItem.optJSONObject("thumbnails");
                    thumbing = thumbing.optJSONObject("default");
                    try {
                        title = currentItem.getString("title");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        thumb = thumbing.getString("url");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    titleNames.get(i-1).setText(title.toString());
                    try {
                        drawableFromUrl(thumb, thumbnails.get(i-1));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    current = i;
                }
//                JSONObject title1 = yt1.optJSONObject("title");
                Log.d("myapp", "total of " + trendItems.length());
                Log.d("myapp", "thumbnail URL " + thumb);
//                Log.d("myapp", "method response  " + );
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void getItems() // just a quick way to grab all the necessary views without clogging up the oncreate method
    {
        trending_one = findViewById(R.id.trending_card1);
        trending_two = findViewById(R.id.trending_card2);
        trending_three = findViewById(R.id.trending_card3);
        trending_four = findViewById(R.id.trending_card4);
        trending_five = findViewById(R.id.trending_card5);
        trending_six = findViewById(R.id.trending_card6);
        trending_seven = findViewById(R.id.trending_card7);
        trending_eight = findViewById(R.id.trending_card8);
        video_name1 = findViewById(R.id.trend_text1);
        video_name2 = findViewById(R.id.trend_text2);
        video_name3 = findViewById(R.id.trend_text3);
        video_name4 = findViewById(R.id.trend_text4);
        video_name5 = findViewById(R.id.trend_text5);
        video_name6 = findViewById(R.id.trend_text6);
        video_name7 = findViewById(R.id.trend_text7);
        video_name8 = findViewById(R.id.trend_text8);
        thumbnail_1 = findViewById(R.id.thumbnail_trend1);
        thumbnail_2 = findViewById(R.id.thumbnail_trend2);
        thumbnail_3 = findViewById(R.id.thumbnail_trend3);
        thumbnail_4 = findViewById(R.id.thumbnail_trend4);
        thumbnail_5 = findViewById(R.id.thumbnail_trend5);
        thumbnail_6 = findViewById(R.id.thumbnail_trend6);
        thumbnail_7 = findViewById(R.id.thumbnail_trend7);
        thumbnail_8 = findViewById(R.id.thumbnail_trend8);
        titleNames.add(video_name1);
        titleNames.add(video_name2);
        titleNames.add(video_name3);
        titleNames.add(video_name4);
        titleNames.add(video_name5);
        titleNames.add(video_name6);
        titleNames.add(video_name7);
        titleNames.add(video_name8);
        thumbnails.add(thumbnail_1);
        thumbnails.add(thumbnail_2);
        thumbnails.add(thumbnail_3);
        thumbnails.add(thumbnail_4);
        thumbnails.add(thumbnail_5);
        thumbnails.add(thumbnail_6);
        thumbnails.add(thumbnail_7);
        thumbnails.add(thumbnail_8);
        cards.add(trending_one);
        cards.add(trending_two);
        cards.add(trending_three);
        cards.add(trending_four);
        cards.add(trending_five);
        cards.add(trending_six);
        cards.add(trending_seven);
        cards.add(trending_eight);
    }
    @SuppressLint("StaticFieldLeak")
    public void drawableFromUrl(String url, ImageView img) throws java.net.MalformedURLException, java.io.IOException {

        new AsyncTask<String, Integer, Drawable>(){
            // This method uses the URL gathered from the API call and finds it over the internet, and turns it into a temporary Drawable to use
            @Override
            protected Drawable doInBackground(String... strings) {
                Bitmap bmp = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bmp = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BitmapDrawable(bmp);
            }

            protected void onPostExecute(Drawable result) {

                img.setImageDrawable(result);

            }

        }.execute();
    }
}
