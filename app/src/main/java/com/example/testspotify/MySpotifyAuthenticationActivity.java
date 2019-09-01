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
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Album;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MySpotifyAuthenticationActivity extends AppCompatActivity {

    List<lTrack> sampleTrackList = new ArrayList<>();

    private void fillTrackList(){
        for(int i = 0; i < 30; i++) {
            sampleTrackList.add(new lTrack("Still Woozy", "Lava", "Lately EP"));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spotify_authentication);

        fillTrackList();
        SimpleAdapter adapter = new SimpleAdapter(sampleTrackList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.songList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void requestTopSongs(String tok) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://api.spotify.com/v1/me/top/tracks";
        //String url = "https://www.google.com";

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Success",response);
                        TextView text=findViewById(R.id.songText);
                        text.setText("Songs Pulled: " + response);
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
        requestTopSongs(tok);
    }

}


