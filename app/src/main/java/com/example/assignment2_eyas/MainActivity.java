package com.example.assignment2_eyas;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment2_eyas.ui.Race;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView profile;
    int[] flags ={R.drawable.bahrain,R.drawable.saudi,R.drawable.australia,
            R.drawable.azerbaijan,R.drawable.usa, R.drawable.monaco,
            R.drawable.spain,  R.drawable.canada,R.drawable.austria,R.drawable.uk,
            R.drawable.hungary,R.drawable.belgian, R.drawable.dutch,R.drawable.italy,
            R.drawable.singapore,R.drawable.japan,R.drawable.qatar, R.drawable.usa,
            R.drawable.mexico,R.drawable.brazil, R.drawable.usa,R.drawable.uae
    };

    String[] tracks={
            "https://mclaren.bloomreach.io/delivery/resources/content/gallery/mclaren-racing/legacy/static_maps/bahrain-outer-b.png",
            "https://static.thenounproject.com/png/5497712-200.png",
            "https://openclipart.org/image/800px/291825",
            "https://en.gpvwc.com/wiki/images/archive/e/e5/20180414161226%21Baku_City_Circuit.png",
            "https://sportsbase.io/images/gpfans/copy_620x348/b0bf8d68c78b77b905f3bef46157d19912cd1c4d.png",
            "https://upload.wikimedia.org/wikipedia/commons/e/e8/Circuit_de_Monaco_2004-2014.png",
            "https://openclipart.org/image/800px/291822",
            "https://openclipart.org/image/800px/291820",
            "https://openclipart.org/image/800px/291818",
            "https://www.fiaformula4.com/content/uploads/2022/03/Silverstone-GP-1024x921.png",
            "https://openclipart.org/image/800px/291816",
            "https://openclipart.org/image/800px/291815",
            "https://en.gpvwc.com/wiki/images/9/9a/Circuit_Park_Zandvoort.png",
            "https://openclipart.org/image/800px/291813",
            "https://openclipart.org/image/800px/291809",
            "https://openclipart.org/image/800px/291798",
            "https://upload.wikimedia.org/wikipedia/commons/0/0f/Losail_International_Circuit.png",
            "https://openclipart.org/image/800px/291803",
            "https://openclipart.org/image/800px/291797",
            "https://openclipart.org/image/800px/291800",
            "https://media.formula1.com/image/upload/f_auto/q_auto/v1677249932/content/dam/fom-website/2018-redesign-assets/Track%20icons%204x3/Las%20Vegas.png",
            "https://upload.wikimedia.org/wikipedia/commons/c/cc/Yas_Island_Circuit_map.png"
    };

    @Override
    protected void onResume() {// in case of a logout, the user cant start the activity again without logging in
        super.onResume();
        if(LoginActivity.loggedInUser==null){
            finish();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView txt = findViewById(R.id.text);
        listView=findViewById(R.id.list);
        profile=findViewById(R.id.profile);


        StrictMode.ThreadPolicy policy;
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Button teams=findViewById(R.id.teams);
        teams.setOnClickListener(e->{
            Intent intent= new Intent(MainActivity.this,TeamsActivity.class);
            startActivity(intent);
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ergast.com/api/f1/2023.json";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                          JSONObject mainObj = jsonObject.getJSONObject("MRData");
                          JSONObject raceTable = mainObj.getJSONObject("RaceTable");
                          JSONArray races = raceTable.getJSONArray("Races");
                          List<Race> races1  = new ArrayList<>();

                            for(int i=0 ; i<races.length();i++){
                                JSONObject race =  races.getJSONObject(i); //
                                int round =race.getInt("round");
                                String raceName = race.getString("raceName");
                                String date = race.getString("date");
                                String time = race.getString("time");
                                races1.add(new Race(round,raceName,date,time,getCircut(i)));
                            }


                            CustomAdapter ca = new CustomAdapter();
                            ca.setData(races1);
                            listView.setAdapter(ca);

                            AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
                                Intent intent = new Intent(MainActivity.this, RaceActivity.class);
                                intent.putExtra("race_name", ca.getItem(position).getName());
                                intent.putExtra("race_round", ca.getItem(position).getRound());
                                intent.putExtra("race_date", ca.getItem(position).getDate());
                                intent.putExtra("race_time", ca.getItem(position).getTime());
                                intent.putExtra("track",ca.getItem(position).getTrack());
                                intent.putExtra("pos",position);
                                startActivity(intent);
                            };
                            listView.setOnItemClickListener(itemClickListener);

                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","api error");
            }
        });

        queue.add(stringRequest);

        profile.setOnClickListener(e->{
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);
        });

    }

    private String getCircut(int i){
        return tracks[i];
    }

private class CustomAdapter extends BaseAdapter {
        private List<Race> races;

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Race getItem(int position) {
        return races.get(position);
    }

    @Override
    public long getItemId(int position) {
        return flags[position];
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View temp = getLayoutInflater().inflate(R.layout.row,null);
        TextView t = temp.findViewById(R.id.name);
        ImageView img = temp.findViewById(R.id.imageView);

        t.setText(races.get(position).getName());
        img.setImageResource(flags[position]);
        return temp;
    }

    public void setData(List<Race> races1) {
        races=races1;
    }
}

}