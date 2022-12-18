package com.example.spotifynfccreator.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.spotifynfccreator.spotifyresponse.TrackItem;

import java.io.IOException;
import java.io.InputStream;

public class SpotifyImageLoader extends AsyncTask<String, Void, Bitmap>
{

  public SpotifyImageResponse resp = null;

  public SpotifyImageLoader(SpotifyImageResponse resp)
  {
    this.resp = resp;
  }

  @Override protected Bitmap doInBackground(String... items)
  {
    Bitmap image = null;
    try
    {
      InputStream in = new java.net.URL(items[0]).openStream();
      image = BitmapFactory.decodeStream(in);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return image;
  }

  @Override protected void onPostExecute(Bitmap bitmap)
  {
    resp.processFinish(bitmap);
  }
}
