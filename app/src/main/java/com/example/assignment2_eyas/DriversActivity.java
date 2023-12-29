package com.example.assignment2_eyas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class DriversActivity extends AppCompatActivity {
    private TextView textView,driver1,driver2;
    private ImageView logo,flag,driver1Img,driver2Img;
    //array of teams, match names from second api

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        logo = findViewById(R.id.logo);
        flag = findViewById(R.id.flag);
        textView = findViewById(R.id.teamName);
        Intent intent =getIntent();
        String s = intent.getExtras().getString("tname");
        int f = intent.getExtras().getInt("flag");
        String l = intent.getExtras().getString("logo");
        logo.setImageDrawable(LoadImageFromWebOperations(l));
        flag.setImageResource(f);
        textView.setText(intent.getExtras().getString("tname"));
        driver1 = findViewById(R.id.driver1);
        driver2 = findViewById(R.id.driver2);
        driver1Img = findViewById(R.id.driver1Img);
        driver2Img = findViewById(R.id.driver2Img);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openf1.org/v1/drivers?session_key=9158&team_name="+s;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(s);
                        try {
                            JSONArray drivers = new JSONArray(response);//no need for main obj according to api

                            for (int i = 0; i < drivers.length(); i++) {
                                JSONObject driverJson = drivers.getJSONObject(i);
                                String name = driverJson.getString("full_name");
                                String country = driverJson.getString("country_code");
                                int number = driverJson.getInt("driver_number");
                                String img = driverJson.getString("headshot_url");
                                if(i==0){
                                    driver1.setText(name+" \n"+number+" \n"+country);
                                    driver1Img.setImageDrawable(LoadImageFromWebOperations(img));
                                }else if (i==1){
                                    driver2.setText(name+" \n"+number+" \n"+country);
                                    driver2Img.setImageDrawable(LoadImageFromWebOperations(img));
                                }//3rd driver ignored due to inactivity

                            }

                            } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);

    }
}