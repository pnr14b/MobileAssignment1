package com.example.testspotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PullActivity extends AppCompatActivity {




    public List<lTrack> parseJson(JSONObject obj){
        ArrayList<lTrack> tracks = new ArrayList<>();
        lTrack t;
        try {
            JSONArray items = obj.getJSONArray("items");
            for(int i = 0; i < items.length();i++){
                t = new lTrack();

                String song = items.getJSONObject(i).getJSONObject("track").getString("name");
                String artist = items.getJSONObject(i).getJSONObject("track").getJSONArray("artists").getJSONObject(0).getString("name");

                System.out.println(song + " " + artist);
                tracks.add(new lTrack(artist,song));


            }
        }
        catch (JSONException e){

        }


        return tracks;


    }


    List<lTrack> tracks = new ArrayList<>();

    public void getData(){

        Intent currentIntent = getIntent();
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://testspotify-42d61.firebaseio.com/");
        //Query q = db.getReference().child(currentIntent.getStringExtra("USER"));
        String username = currentIntent.getStringExtra("USER");

        db.getReference(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tracks.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lTrack t = postSnapshot.getValue(lTrack.class);
                    tracks.add(t);

                    Log.d("DEBUG",t.getArtist()+":"+t.getSong());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        getData();

        System.out.println(tracks.size());
        for (int i = 0; i < tracks.size();i++){
            Log.d("DEejfhk",tracks.get(i).getArtist()+":"+tracks.get(i).getSong());
        }


        SimpleAdapter adapter = new SimpleAdapter(tracks);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pulledSongs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }


}
