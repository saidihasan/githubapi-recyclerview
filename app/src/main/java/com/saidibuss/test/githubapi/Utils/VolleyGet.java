package com.saidibuss.test.githubapi.Utils;

import android.content.Context;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class VolleyGet {

    private int limitInt = 10;

    public int getSearchLimit(Context context) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_LIMIT_QUERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jo = jsonObject.getJSONObject("resources");
                    JSONObject joSearch = jo.getJSONObject("search");

                    limitInt = joSearch.getInt(Config.KEY_LIMIT);


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

    return limitInt;
    }
}
