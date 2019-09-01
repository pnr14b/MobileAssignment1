package com.example.testspotify;

public class lTrack {
    private String artist;
    private String song;
    private String album;

    public lTrack(String artist, String song){
        this.artist = artist;

        this.song = song;
    }

    public lTrack(){

    }

    public String getSong(){
        return this.song;
    }
    
    public String getArtist(){
        return this.artist;
    }


}
