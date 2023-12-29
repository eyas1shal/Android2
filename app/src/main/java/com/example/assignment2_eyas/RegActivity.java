package com.example.assignment2_eyas;

import static android.provider.SyncStateContract.Columns.DATA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2_eyas.ui.Team;
import com.example.assignment2_eyas.ui.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RegActivity extends AppCompatActivity {

    private EditText uname,password,confirmPass;
    Spinner sp;
    private Button reg,back;
    private SharedPreferences prefs;
    private  SharedPreferences.Editor editor;
    private  Gson gson;
    private  List<User> users;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        uname= findViewById(R.id.uname);
        password=findViewById(R.id.password);
        confirmPass=findViewById(R.id.confirmPass);
        sp=(Spinner) findViewById(R.id.favTeam);
        reg=findViewById(R.id.log);
        back=findViewById(R.id.back);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.constructors));
        sp.setAdapter(adapter);


        reg.setOnClickListener(e->{
            //check
            if(uname.getText().toString().equals("")
                    || password.getText().toString().equals("")
                    || confirmPass.getText().toString().equals("") || password.getText().length()<8
                    || uname.getText().length()>25){
                Toast.makeText(this, "Enter all fields with a password of 8 length", Toast.LENGTH_SHORT).show();
                return;
            }
            if(! password.getText().toString().equals(confirmPass.getText().toString())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }
            //check if user exists
            setupSharedPrefs();
            loadUsers();
            for(User u: users){
                if(u.getUname().equals(uname.getText().toString())){
                    Toast.makeText(this, "User name already taken", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //add user
            try {
                regUser();

                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                intent.putExtra("uname", uname.getText());//for better filling
                intent.putExtra("pass", password.getText());
                startActivity(intent);
            }catch (Exception x){
                Toast.makeText(this, "Error while Registering", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(e->{
            Intent intent= new Intent(RegActivity.this,LoginActivity.class);
            intent.putExtra("uname",uname.getText());//for better filling
            intent.putExtra("pass",password.getText());
            startActivity(intent);
        });



    }
    private void setupSharedPrefs() {
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }
    private void loadUsers(){
        gson = new Gson();
        //load users
        users = new ArrayList<>();
        String str = prefs.getString(DATA, "");
        if (!str.equals("")) {
            User[] users1 = gson.fromJson(str, User[].class);
            for (int i = 0; i < users1.length; i++) {
                users.add(users1[i]);
            }
        }
    }
    private void regUser(){

        String uname = this.uname.getText().toString();
        String pass = this.password.getText().toString();

        Team favTeam = new Team(this.sp.getSelectedItem().toString(),logo[sp.getSelectedItemPosition()],"");
        //add user
        users.add(new User(uname,pass,favTeam));
        //save
        String usersString = gson.toJson(users);
        editor.putString(DATA, usersString);
        editor.apply();

    }

}