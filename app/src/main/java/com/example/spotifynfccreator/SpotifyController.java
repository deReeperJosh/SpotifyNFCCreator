package com.example.spotifynfccreator;

import android.util.Log;

import java.io.IOException;

import com.example.spotifynfccreator.spotifyresponse.SpotifyData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyController
{

  static final String BASE_URL = "https://api.spotify.com/v1/";

  String accessToken;

  public SpotifyController(String access_token)
  {
    this.accessToken = access_token;
  }

  public SpotifyData start(String title, String type)
  {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    SpotifyService spotifyService = retrofit.create(SpotifyService.class);

    Call<SpotifyData> spotify =
            spotifyService.listSpotify(title, type, "NZ", "10", "Bearer " + accessToken);
    try
    {
      Response<SpotifyData> data = spotify.execute();
      if (data.isSuccessful())
      {
        return data.body();
      }
      else
      {
        Log.e("Spotify", data.errorBody().string());
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}