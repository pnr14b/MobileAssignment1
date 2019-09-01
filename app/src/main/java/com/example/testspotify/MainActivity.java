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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testspotify.ui.login.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Album;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity<stringRequest> extends AppCompatActivity {

    private static final String CLIENT_ID = "783249b827704cbdab0b62f069fe51c4";
    private static final String REDIRECT_URI = "youcustomprotocol://callback";
    public SpotifyAppRemote mSpotifyAppRemote;
    FirebaseDatabase database;
    DatabaseReference myRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance("https://testspotify-42d61.firebaseio.com/");
        database.getReference("hello").setValue("We pushing from wyndham");
    }


    private static final int REQUEST_CODE = 1337;

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-top-read","user-library-read","user-read-recently-played","user-modify-playback-state"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


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
                    Intent intent1 = new Intent(this, MySpotifyAuthenticationActivity.class);
                    intent1.putExtra("AUTH_TOKEN", response.getAccessToken() );

                    TextView text=findViewById(R.id.middle_text);
                    text.setText("Authenticated");

                    //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
                    Log.d( "TESTT", response.getAccessToken());

                    startActivity(intent1);

                    //sendRequest(response.getAccessToken());

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