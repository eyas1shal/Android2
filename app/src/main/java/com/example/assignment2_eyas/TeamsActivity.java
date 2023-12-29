package com.example.assignment2_eyas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.assignment2_eyas.ui.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private DecimalFormat df = new DecimalFormat("#.##");
    ListView listView;
    List<Team> teams1;
    Button racesBtn;
    ImageView profile;

    String[] logo={
            "https://seeklogo.com/images/A/alfa-romeo-logo-BC9622CF7F-seeklogo.com.png",
            "https://logodownload.org/wp-content/uploads/2022/03/alphatauri-logo-0.png",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Alpine_F1_Team_Logo.svg/2233px-Alpine_F1_Team_Logo.svg.png",
            "https://branditechture.agency/brand-logos/wp-content/uploads/2022/10/Aston-Martin-Cognizant-Formula-One-Team-1024x695.png",
            "https://pngimg.com/d/ferrari_PNG102798.png",
            "https://upload.wikimedia.org/wikipedia/commons/d/d4/Logo_Haas_F1.png",
            "https://upload.wikimedia.org/wikipedia/en/thumb/6/66/McLaren_Racing_logo.svg/1280px-McLaren_Racing_logo.svg.png",
            "https://i.pinimg.com/originals/2a/94/46/2a944675838555cab1c495fc21822ed9.png",
            "https://i.pinimg.com/originals/fa/0d/4c/fa0d4cf5d95ccadfc7cc8147654f8518.png",
            "https://upload.wikimedia.org/wikipedia/commons/f/f9/Logo_Williams_F1.png"
    };
    private int[] flags ={
            R.drawable.italy,
            R.drawable.austria,
            R.drawable.france,
            R.drawable.uk,
            R.drawable.italy,
            R.drawable.usa,
            R.drawable.uk,
            R.drawable.uk,
            R.drawable.austria,
            R.drawable.uk
    };



    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    protected void onResume() {// in case of a logout, the user cant start the activity again without logging in
        super.onResume();
        if(LoginActivity.loggedInUser==null){
            finish();
            Intent intent = new Intent(TeamsActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        listView=findViewById(R.id.list);
        racesBtn=findViewById(R.id.Races);
        profile=findViewById(R.id.profile1);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ergast.com/api/f1/2023/constructors.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject mainObj = jsonObject.getJSONObject("MRData");
                            JSONObject driverTable = mainObj.getJSONObject("ConstructorTable");
                            JSONArray teams = driverTable.getJSONArray("Constructors");

                            teams1 = new ArrayList<>();

                            for(int i=0 ; i<teams.length();i++){
                                JSONObject team =  teams.getJSONObject(i); //
                                String name = team.getString("name");
                                String country = team.getString("nationality");

                                Team temp = new Team(name,country,logo[i]);
                                teams1.add(temp);
                            }

                            CustomAdapter ca = new CustomAdapter();
                            ca.setData(teams1);
                            listView.setAdapter(ca);

                            AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
                                Intent intent = new Intent(TeamsActivity.this, DriversActivity.class);
                                String tname = ca.getItem(position).getTeam_name();
                                //fix both redbull and alpine
                                if(tname.equals("Red Bull")){
                                    tname="Red Bull Racing";
                                }
                                if(tname.equals("Alpine F1 Team")){
                                    tname="Alpine";
                                }
                                intent.putExtra("tname", tname);
                                intent.putExtra("country",ca.getItem(position).getCountry());
                                intent.putExtra("flag",flags[position]);
                                intent.putExtra("logo",logo[position]);
                                intent.putExtra("pos",position);//maybe no need// maybe for fav
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
                Log.d("error",error.toString());
            }

        });

        queue.add(stringRequest);


        racesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private class CustomAdapter extends BaseAdapter {
        private List<Team> teams;

        @Override
        public int getCount() {
            return logo.length;
        }

        @Override
        public Team getItem(int position) {
            return teams.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View temp = getLayoutInflater().inflate(R.layout.row, null);
            TextView t = temp.findViewById(R.id.name);
            ImageView img = temp.findViewById(R.id.imageView);

            t.setText(teams.get(position).getTeam_name());

            img.setImageDrawable(LoadImageFromWebOperations(logo[position]));
            return temp;
        }

        public void setData(List<Team> teams1) {
            teams = teams1;
        }
    }
    }