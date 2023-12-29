package com.example.assignment2_eyas.ui;

public class User {

    private String uname;
    private String pass;
    private Team favTeam;

    public User(String uname, String pass, Team favTeam) {
        this.uname = uname;
        this.pass = pass;
        this.favTeam = favTeam;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Team getFavTeam() {
        return favTeam;
    }

    public void setFavTeam(Team favTeam) {
        this.favTeam = favTeam;
    }
}
