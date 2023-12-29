package com.example.assignment2_eyas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

//add logout and delete user, also link to fav team activity
public class ProfileActivity extends AppCompatActivity {
    TextView txt,teamName;
    ImageView logo;
    Button logout,delete,restPass,changeTeam;
    EditText newPass,confirmPass;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txt= findViewById(R.id.txtpro);
        txt.setText(LoginActivity.loggedInUser.getUname());
        teamName=findViewById(R.id.teamname);
        logo=findViewById(R.id.teamLogo);
        logout=findViewById(R.id.logOut);
        delete=findViewById(R.id.del);
        restPass=findViewById(R.id.reset);
        newPass=findViewById(R.id.editPassword);
        confirmPass=findViewById(R.id.editPassword2);
        sp=findViewById(R.id.spinner2);
        changeTeam=findViewById(R.id.newTeam);

        teamName.setText(LoginActivity.loggedInUser.getFavTeam().getTeam_name());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.constructors));
        sp.setAdapter(adapter);

        logo.setImageDrawable(LoadImageFromWebOperations(LoginActivity.loggedInUser.getFavTeam().getTeam_logo()));


        logout.setOnClickListener(e->{
            LoginActivity.loggedInUser=null;
            Intent intent= new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
        });
        delete.setOnClickListener(e->{
            LoginActivity.delUser();

            Intent intent= new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        changeTeam.setOnClickListener(e->{
            LoginActivity.changeTeam(sp.getSelectedItem().toString(),sp.getSelectedItemPosition());
            teamName.setText(LoginActivity.loggedInUser.getFavTeam().getTeam_name());
            logo.setImageDrawable(LoadImageFromWebOperations(LoginActivity.loggedInUser.getFavTeam().getTeam_logo()));
        });
        restPass.setOnClickListener(e->{
            if(newPass.getText().toString().equals("")
                    || confirmPass.getText().toString().equals("") || newPass.getText().length()<8
                    || newPass.getText().length()>25){
                Toast.makeText(this, "Enter all fields with a minimum of 8 length", Toast.LENGTH_SHORT).show();
                return;
            }
            if(! newPass.getText().toString().equals(confirmPass.getText().toString())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }
            LoginActivity.changePass(newPass.getText().toString());
        });

    }
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
    protected void onPause() {

        super.onPause();
        this.finish();//destroy activity so user wont press back and reLogin without password
    }
}