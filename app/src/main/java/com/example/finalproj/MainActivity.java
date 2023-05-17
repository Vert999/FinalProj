package com.example.finalproj;

import static android.app.PendingIntent.getActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    CardView trending;
    CardView music;
    FragmentTransaction xact;
    FragmentManager fragMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trending = findViewById(R.id.card_view);
        music = findViewById(R.id.music_card);
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("text", "value");
        editor.commit();
        fragMgr = getFragmentManager();
        xact = fragMgr.beginTransaction();
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TrendingActivity.class));
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MusicActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.backItem:
                Toast.makeText(this, "Already at Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuItem:
                Toast.makeText(this, "Options ready", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.historyItem:
                Toast.makeText(this, "History ready", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.modeItem:
                Toast.makeText(this, "Dark Mode ready", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAPI()
    {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://www.googleapis.com/youtube/v3/videos?id=7lCDEYXw3mM&key=AIzaSyDNw6r-xqZOS-WolHQqrP0PGSoYDBcmg_0&part=snippet,contentDetails,statistics,status", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ;
//                    JSONObject STdata = response.optJSONObject("");
                Log.d("myapp", " The response is " + response);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myapp", "Something went wrong");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
