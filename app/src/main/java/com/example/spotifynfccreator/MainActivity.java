package com.example.spotifynfccreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.spotifynfccreator.spotifyresponse.SpotifyData;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity
{
  private static final int REQUEST_CODE = 1337;
  private static final String REDIRECT_URI = "https://com.example.spotifynfccreator/callback";
  static final String CLIENT_ID = "2578df2bd8ca45ad83d33e2bb0d492df";

  SpotifyData data;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onStart()
  {
    super.onStart();
    AuthorizationRequest.Builder builder =
            new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN,
                    REDIRECT_URI);

    builder.setScopes(new String[]{"streaming"});
    AuthorizationRequest request = builder.build();

    AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);

  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent)
  {
    super.onActivityResult(requestCode, resultCode, intent);

    // Check if result comes from the correct activity
    if (requestCode == REQUEST_CODE)
    {
      AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

      switch (response.getType())
      {
        // Response was successful and contains auth token
        case TOKEN:
          String accessToken = response.getAccessToken();
          Log.d("Spotify", accessToken);
          Intent searchIntent = new Intent(MainActivity.this, SpotifyInput.class);
          searchIntent.putExtra("AccessToken", accessToken);
          startActivity(searchIntent);
          break;

        // Auth flow returned an error
        case ERROR:
          // Handle error response
          break;

        // Most likely auth flow was cancelled
        default:
          // Handle other cases
      }
    }
  }
}