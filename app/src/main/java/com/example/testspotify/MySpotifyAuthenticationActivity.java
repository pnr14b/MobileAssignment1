package com.example.testspotify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testspotify.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Album;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MySpotifyAuthenticationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spotify_authentication);
        database = FirebaseDatabase.getInstance("https://testspotify-42d61.firebaseio.com/");

    }

    public String USER;


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


    List<lTrack> sampleTrackList = new ArrayList<>();
    public FirebaseDatabase database;
    String jsonString;

    public void requestTopSongs(String tok, String user) {

        TextView text=findViewById(R.id.songText);
        text.setText("Pulling Songs");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://api.spotify.com/v1/me/player/recently-played";
        //String url = "https://www.google.com";

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject resp = new JSONObject(response);
                            sampleTrackList = parseJson(resp);
                            jsonString = response;

                            text.setText("Songs Pulled");

                            database = FirebaseDatabase.getInstance("https://testspotify-42d61.firebaseio.com/");
                            database.getReference(user).setValue(sampleTrackList);
                        }
                        catch (JSONException e ){
                            Log.d("EXCEPTION","Could not parse response into object");
                        }
                       // Log.d("Success",response);
                        //TextView text=findViewById(R.id.songText);
                        //text.setText("Songs Pulled: " + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("ERROR","error => "+error.toString());
                        TextView text=findViewById(R.id.songText);
                        text.setText("Error");
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String auth = "Bearer " + tok;
                //+ Base64.getEncoder().encodeToString(tok.getBytes());
                params.put("Authorization", auth);



                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(getRequest);
    }

    public void pullTopSongs(View view){
        Intent intent1 = getIntent();
        String tok = intent1.getStringExtra("AUTH_TOKEN");
        String user = intent1.getStringExtra("USER");
        requestTopSongs(tok,user);

        SimpleAdapter adapter = new SimpleAdapter(sampleTrackList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.songList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}


