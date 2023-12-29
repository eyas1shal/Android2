package com.example.assignment2_eyas;

import static android.provider.SyncStateContract.Columns.DATA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2_eyas.ui.Team;
import com.example.assignment2_eyas.ui.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText uname, password;
    private Button login, reg;
    private boolean flag = false;
    private SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    public static final String FLAG = "FLAG";
    private CheckBox auto;
    private static List<User> users;
    private static Gson gson;
    public static User loggedInUser;
    static String[] logo={
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        login = findViewById(R.id.log);

        auto = findViewById(R.id.rme);
        reg = findViewById(R.id.reg);
        setupSharedPrefs();
        checkPrefs();

        try {//The user that just registered will have his username and password filled
            Intent fromReg = getIntent();
            if (!fromReg.getExtras().get("uname").equals("")
                    && !fromReg.getExtras().get("pass").equals("")) {
                uname.setText(fromReg.getExtras().get("uname").toString());
                password.setText(fromReg.getExtras().get("pass").toString());
            }
        } catch (Exception x) {
            Log.d("error", x.getMessage());
        }


        reg.setOnClickListener(e -> {
            Intent intent = new Intent(LoginActivity.this, RegActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(e -> {
            if (auto.isChecked()) {
                if (!flag) {
                    editor.putString(NAME, uname.getText().toString());
                    editor.putString(PASS, password.getText().toString());
                    editor.putBoolean(FLAG, true);
                    editor.commit();
                }
            } else {
                editor.putString(NAME, "");
                editor.putString(PASS, "");
                editor.putBoolean(FLAG, false);
                editor.commit();
            }
            //login in from gson
            loadUsers();
            for (User u : users) {
                if (u.getUname().equals(uname.getText().toString())
                        && u.getPass().equals(password.getText().toString())) {
                    //login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    loggedInUser = u;
                    intent.putExtra("uname", uname.getText().toString());
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Manage your profile by clicking on the helmet", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //alert the user that he is not registered
            Toast.makeText(this, "User not registered", Toast.LENGTH_SHORT).show();

        });


    }

    private void checkPrefs() {
        flag = prefs.getBoolean(FLAG, false);

        if (flag) {
            String name = prefs.getString(NAME, "");
            String ps = prefs.getString(PASS, "");
            uname.setText(name);
            password.setText(ps);
            auto.setChecked(true);
        }
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void loadUsers() {
        //load users from gson
        gson = new Gson();
        users = new ArrayList<>();
        String str = prefs.getString(DATA, "");
        if (!str.equals("")) {
            User[] users1 = gson.fromJson(str, User[].class);
            for (int i = 0; i < users1.length; i++) {
                users.add(users1[i]);
            }
        }
    }
    public static void delUser(){
        //delete user
        users.remove(loggedInUser);
        loggedInUser=null;
        //save
        String usersString = gson.toJson(users);
        editor.putString(DATA, usersString);
        editor.apply();
    }
    public static void changeTeam(String teamName,int pos){
        //change team
        loggedInUser.setFavTeam(new Team(teamName,logo[pos],""));
        //save
        String usersString = gson.toJson(users);
        editor.putString(DATA, usersString);
        editor.apply();
    }
    public static void changePass(String pass){
        //change pass
        loggedInUser.setPass(pass);
        //save
        String usersString = gson.toJson(users);
        editor.putString(DATA, usersString);
        editor.apply();
    }
}