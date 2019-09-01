package com.example.testspotify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity<stringRequest> extends AppCompatActivity {

    private static final String CLIENT_ID = "783249b827704cbdab0b62f069fe51c4";
    private static final String REDIRECT_URI = "youcustomprotocol://callback";
    public SpotifyAppRemote mSpotifyAppRemote;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private static final int REQUEST_CODE = 1337;

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-top-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }




    public void sendRequest(String tok) {



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
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
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


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);


            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    /*
                    Intent intent1 = new Intent(this, MySpotifyAuthenticationActivity.class);
                    startActivity(intent1);
                    */


                    TextView text=findViewById(R.id.middle_text);
                    text.setText("Authenticated");

                    //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
                    Log.d( "TESTT", response.getAccessToken());

                    sendRequest(response.getAccessToken());

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    TextView text1=findViewById(R.id.middle_text);
                    text1.setText("Error");
                    String logMessage = "Auth error: " + response.getError();
                    Log.e( "Error: ", logMessage);
                    break;

                // Most likely auth flow was cancelled
                default:
                    TextView text2=findViewById(R.id.middle_text);
                    text2.setText("Default");
                    // Handle other cases
            }
        }
    }





}