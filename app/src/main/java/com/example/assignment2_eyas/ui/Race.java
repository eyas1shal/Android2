package com.example.assignment2_eyas.ui;

public class Race {
    private int round;
    private String name;
    private String date;
    private String time;
    private String track;


    public Race(int round, String name, String date, String time,String track) {
        this.round = round;
        this.name = name;
        this.date = date;
        this.time = time;
        this.track= track;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return round + "- " + name;
    }
}
