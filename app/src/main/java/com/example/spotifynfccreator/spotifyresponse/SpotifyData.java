
package com.example.spotifynfccreator.spotifyresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotifyData {

    @SerializedName("albums")
    @Expose
    private Albums albums;
    @SerializedName("tracks")
    @Expose
    private Tracks tracks;

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

}
