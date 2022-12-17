
package com.example.spotifynfccreator.spotifyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalUrls {

    @SerializedName("spotify")
    @Expose
    private String spotify;

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    @Override public String toString()
    {
        return "ExternalUrls{" +
                "spotify='" + spotify + '\'' +
                '}';
    }
}
