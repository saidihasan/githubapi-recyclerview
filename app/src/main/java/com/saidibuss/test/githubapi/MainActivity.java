package com.saidibuss.test.githubapi;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saidibuss.test.githubapi.Adapters.GithubUserAdapter;
import com.saidibuss.test.githubapi.Object.GithubUser;
import com.saidibuss.test.githubapi.Utils.Config;
import com.saidibuss.test.githubapi.Utils.VolleyGet;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String username;

    private List<GithubUser> githubUserLists;

    int page = 1, limit;

    private Parcelable recyclerViewState;

    TextView limitTv;

    VolleyGet volleyGet;

    RelativeLayout relativeLayout;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        githubUserLists = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        limitTv = findViewById(R.id.limit_tv);

        relativeLayout = findViewById(R.id.relativeLayout);
        linearLayout = findViewById(R.id.first_start);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                if(!recyclerView.canScrollVertically(1)) {
                    if(limit == 0){
                        Snackbar snackbar = Snackbar.make(relativeLayout,"Wait for a few minute", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    page++;
                    findUserNext(page);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // Handling limit
        volleyGet = new VolleyGet();
        limitTv.setVisibility(View.INVISIBLE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                linearLayout.setVisibility(View.GONE);
                githubUserLists.clear();
                if(volleyGet.getSearchLimit(getApplicationContext()) == 0){
                    Snackbar snackbar = Snackbar.make(relativeLayout,"Wait for a few minute", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                username = s;
                findUser(username);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void findUser(String username){
        String URL = Config.URL_USER_QUERY+username;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    if(jsonArray.length() == 0){
                        Snackbar snackbar = Snackbar.make(relativeLayout,"No users found", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        GithubUser githubUser = new GithubUser(jo.getString(Config.KEY_USERNAME),jo.getString(Config.KEY_IMAGE));
                        githubUserLists.add(githubUser);

                    }

                    adapter = new GithubUserAdapter(githubUserLists,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

        limitTv.setVisibility(View.VISIBLE);
        limit = volleyGet.getSearchLimit(getApplicationContext());
        limitTv.setText("Remaining : "+limit);
    }

    private void findUserNext(int page){
        String URL = Config.URL_USER_QUERY+username+"&page="+page;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        GithubUser githubUser = new GithubUser(jo.getString(Config.KEY_USERNAME),jo.getString(Config.KEY_IMAGE));
                        githubUserLists.add(githubUser);

                    }

                    adapter = new GithubUserAdapter(githubUserLists,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

        limitTv.setVisibility(View.VISIBLE);
        limit = volleyGet.getSearchLimit(getApplicationContext());
        limitTv.setText("Remaining : "+limit);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
