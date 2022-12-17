package com.example.spotifynfccreator;

import android.app.PendingIntent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifynfccreator.adapters.SpotifyTrackAdapter;
import com.example.spotifynfccreator.spotifyresponse.SpotifyData;

public class SpotifyInput extends Activity
{
  NfcAdapter nfcAdapter;
  PendingIntent pendingIntent;

  Button mButton;
  EditText mTitle;
  EditText mType;
  RecyclerView m_recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.spotify_input);
    mButton = findViewById(R.id.button);
    m_recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view)
      {
        mTitle = findViewById(R.id.editTextTrack);
        mType = findViewById(R.id.editTextType);
        String title = mTitle.getText().toString();
        String type = mType.getText().toString();
        Log.d("Spotify", title + ":" + type);
        if (title == "")
        {
          Log.d("Spotify", "No title");
        }
        else if (!type.equals("track") && !type.equals("album") && !type.equals("track,album") &&
                !type.equals("album,track"))
        {
          Log.d("Spotify", "Not correct type");
        }
        else
        {
          String accessToken = getIntent().getStringExtra("AccessToken");
          Log.d("Spotify", "Start Search: " + accessToken);
          AsyncTask retrieve = new RetrieveSpotifyData(new SpotifyDataResponse()
          {
            @Override public void processFinish(SpotifyData spotifyData)
            {
              if (spotifyData != null)
              {
                if (spotifyData.getTracks() != null)
                {
                  SpotifyTrackAdapter adapter = new SpotifyTrackAdapter(spotifyData.getTracks()
                          .getItems(), accessToken);
                  m_recyclerView.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
                }
              }
            }
          }, accessToken, title, type);
          retrieve.execute();
        }
      }
    });
  }
}