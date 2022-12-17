package com.example.spotifynfccreator;

import android.os.AsyncTask;
import android.util.Log;

import com.example.spotifynfccreator.spotifyresponse.SpotifyData;
import com.example.spotifynfccreator.spotifyresponse.TrackItem;

public class RetrieveSpotifyData
        extends AsyncTask<Object, Void, SpotifyData>
{
  String accessToken;
  String title;
  String type;
  SpotifyDataResponse resp = null;

  RetrieveSpotifyData(SpotifyDataResponse resp, String accessToken, String title, String type)
  {
    this.resp = resp;
    this.accessToken = accessToken;
    this.title = title;
    this.type = type;
  }

  @Override protected SpotifyData doInBackground(Object... objects)
  {
    SpotifyController spotifyController = new SpotifyController(this.accessToken);
    return spotifyController.start(this.title, this.type);
  }

  @Override protected void onPostExecute(SpotifyData spotifyData)
  {
    resp.processFinish(spotifyData);
  }
}
