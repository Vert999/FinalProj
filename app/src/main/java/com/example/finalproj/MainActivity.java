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
import android.widget.SearchView;
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
    FragmentTransaction xact;
    FragmentManager fragMgr;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trending = findViewById(R.id.card_view);
        SharedPreferences sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("text", "value");
        editor.commit();
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TrendingActivity.class));

            }
        });

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
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
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
    public boolean onOptionsItemSelected(@NonNull MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.backItem:
                Toast.makeText(this, "Already at Home", Toast.LENGTH_SHORT).show(); // you're already home so whats the need to go back
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }

}
