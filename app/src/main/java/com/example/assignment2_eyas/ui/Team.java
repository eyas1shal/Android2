package com.example.assignment2_eyas.ui;

public class Team {

    private String team_name;
    private String team_logo;
    private String country;

    public Team(String team_name, String team_logo, String country) {
        this.team_name = team_name;
        this.team_logo = team_logo;
        this.country = country;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_logo() {
        return team_logo;
    }

    public void setTeam_logo(String team_logo) {
        this.team_logo = team_logo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
