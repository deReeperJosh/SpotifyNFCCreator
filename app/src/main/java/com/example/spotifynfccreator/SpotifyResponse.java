package com.example.spotifynfccreator;

import com.example.spotifynfccreator.spotifyresponse.SpotifyData;

public interface SpotifyResponse
{
  void processFinish(String accessToken);
}
