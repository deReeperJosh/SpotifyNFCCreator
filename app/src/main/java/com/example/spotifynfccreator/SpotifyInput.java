package com.example.spotifynfccreator;

import android.app.PendingIntent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifynfccreator.adapters.SpotifyAlbumAdapter;
import com.example.spotifynfccreator.adapters.SpotifyTrackAdapter;
import com.example.spotifynfccreator.spotifyresponse.SpotifyData;

import java.util.ArrayList;
import java.util.List;

public class SpotifyInput extends Activity implements AdapterView.OnItemSelectedListener
{
  NfcAdapter nfcAdapter;
  PendingIntent pendingIntent;

  Button mButton;
  EditText mTitle;
  RecyclerView m_recyclerView;
  String m_typeString;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.spotify_input);
    mButton = findViewById(R.id.button);
    m_recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    // Spinner element
    final Spinner spinner = (Spinner) findViewById(R.id.spinner);

    // Spinner click listener
    spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

    // Spinner Drop down elements
    List<String> categories = new ArrayList<String>();
    categories.add("track");
    categories.add("album");

    // Creating adapter for spinner
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    spinner.setAdapter(dataAdapter);

    mButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view)
      {
        mTitle = findViewById(R.id.editTextTrack);
        String title = mTitle.getText().toString();
        String type = m_typeString;
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
                else if (spotifyData.getAlbums() != null)
                {
                  SpotifyAlbumAdapter adapter =
                          new SpotifyAlbumAdapter(spotifyData.getAlbums().getItems(), accessToken);
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

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    // On selecting a spinner item
    String item = parent.getItemAtPosition(position).toString();
    m_typeString = item;
  }

  @Override public void onNothingSelected(AdapterView<?> adapterView)
  {
    m_typeString = "";
  }
}