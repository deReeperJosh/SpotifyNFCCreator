package com.example.spotifynfccreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifynfccreator.MainActivity;
import com.example.spotifynfccreator.R;
import com.example.spotifynfccreator.SpotifyInput;
import com.example.spotifynfccreator.nfc.NFCWriterActivity;
import com.example.spotifynfccreator.spotifyresponse.TrackItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SpotifyTrackAdapter extends RecyclerView.Adapter<SpotifyTrackAdapter.ViewHolder>
{
  public class ViewHolder extends RecyclerView.ViewHolder{

    public TextView m_textView;
    public ImageView m_imageView;
    public Button m_button;

    public ViewHolder(@NonNull View itemView)
    {
      super(itemView);

      m_textView = (TextView) itemView.findViewById(R.id.item_title);
      m_imageView = (ImageView) itemView.findViewById(R.id.item_image);
      m_button = (Button) itemView.findViewById(R.id.nfc_button);
    }
  }

  private List<TrackItem> m_trackItemList;
  private String accessToken;

  public SpotifyTrackAdapter(List<TrackItem> items, String token)
  {
    this.m_trackItemList = items;
    this.accessToken = token;
  }

  @NonNull @Override
  public SpotifyTrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    Log.d("Spotify", m_trackItemList.toString());
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View itemView = inflater.inflate(R.layout.spotify_item_layout, parent, false);

    ViewHolder viewHolder = new ViewHolder(itemView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull SpotifyTrackAdapter.ViewHolder holder, int position)
  {

    TrackItem item = m_trackItemList.get(position);

    TextView textView = holder.m_textView;
    textView.setText(item.getName());
    ImageView imageView = holder.m_imageView;

    SpotifyImageLoader spotifyImageLoader = new SpotifyImageLoader(new SpotifyImageResponse()
    {
      @Override public void processFinish(Bitmap image)
      {
        imageView.setImageBitmap(image);
      }
    });
    spotifyImageLoader.execute(item);

    Button button = holder.m_button;
    button.setOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View view)
      {
        Intent nfcIntent = new Intent(view.getContext(), NFCWriterActivity.class);
        nfcIntent.putExtra("URI", item.getUri());
        nfcIntent.putExtra("AccessToken", accessToken);
        view.getContext().startActivity(nfcIntent);
      }
    });

  }

  @Override public int getItemCount()
  {
    Log.d("Spotify", String.valueOf(m_trackItemList.size()));
    return m_trackItemList.size();
  }
}
