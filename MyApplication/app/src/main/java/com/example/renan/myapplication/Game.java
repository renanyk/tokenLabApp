package com.example.renan.myapplication;


import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by renan on 24/08/2017.
 */

public class Game {
    private int id;
    private String name;
    private String image_url;
    private String release_date;
    private String trailer_url;
    private ArrayList<String> platforms;
    private Bitmap image;
    public Game(int id,String name,String image_url, String release_date,String trailer_url, ArrayList<String> platforms){
        this.id=id;
        this.name = name;
        this.image_url = image_url;
        this.release_date = release_date;
        this.trailer_url = trailer_url;
        this.platforms = platforms;

    }
    public Game(){
        this.id=0;
        this.name = " ";
        this.image_url = " ";
        this.release_date = " ";
        this.trailer_url = " ";
        this.platforms = new ArrayList<>();

    }
    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
}

