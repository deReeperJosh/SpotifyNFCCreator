package com.example.spotifynfccreator.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifynfccreator.R;
import com.example.spotifynfccreator.nfc.NFCWriterActivity;
import com.example.spotifynfccreator.spotifyresponse.AlbumItem;
import com.example.spotifynfccreator.spotifyresponse.TrackItem;

import java.util.List;

public class SpotifyAlbumAdapter extends RecyclerView.Adapter<SpotifyAlbumAdapter.ViewHolder>
{
  public class ViewHolder extends RecyclerView.ViewHolder
  {
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

  private List<AlbumItem> m_albumItemList;
  private String accessToken;

  public SpotifyAlbumAdapter(List<AlbumItem> items, String token)
  {
    this.m_albumItemList = items;
    this.accessToken = token;
  }

  @NonNull @Override
  public SpotifyAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View itemView = inflater.inflate(R.layout.spotify_item_layout, parent, false);

    ViewHolder viewHolder = new ViewHolder(itemView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull SpotifyAlbumAdapter.ViewHolder holder, int position)
  {
    AlbumItem item = m_albumItemList.get(position);

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
    spotifyImageLoader.execute(item.getImages().get(0).getUrl());

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
    return m_albumItemList.size();
  }
}
