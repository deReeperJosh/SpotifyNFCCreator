package com.example.spotifynfccreator;

import com.example.spotifynfccreator.spotifyresponse.SpotifyData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SpotifyService
{
  @GET("search")
  Call<SpotifyData> listSpotify(@Query(value = "q", encoded = true) String searchQuery,
          @Query(value = "type", encoded = true) String type,
          @Query(value = "market", encoded = true) String market,
          @Query(value = "limit", encoded = true) String limit,
          @Header("Authorization") String authorization);
}
