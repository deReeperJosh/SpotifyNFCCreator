package com.example.spotifynfccreator.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.spotifynfccreator.R;
import com.example.spotifynfccreator.SpotifyInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NFCWriterActivity extends AppCompatActivity
{
  final static int MAX_BYTE_LENGTH = 16;

  NfcAdapter nfcAdapter;
  PendingIntent pendingIntent;
  String URI = "";
  String accessToken;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    Log.d("Spotify", "On create write to card");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nfcwriter);
    URI = getIntent().getStringExtra("URI");
    accessToken = getIntent().getStringExtra("AccessToken");

    //Initialise NfcAdapter
    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    //If no NfcAdapter, display that the device has no NFC
    if (nfcAdapter == null)
    {
      Toast.makeText(this, "NO NFC Capabilities",
              Toast.LENGTH_SHORT).show();
      Log.e("Spotify", "No NFC Present");
      finish();
    }
    //Create a PendingIntent object so the Android system can
    //populate it with the details of the tag when it is scanned.
    pendingIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    assert nfcAdapter != null;
    nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
  }

  @Override
  protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    setIntent(intent);
    resolveIntent(intent);
  }

  private void resolveIntent(Intent intent)
  {
    String action = intent.getAction();
    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
    {
      Log.d("Spotify", "Found Card");
      Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
      MifareClassic card = MifareClassic.get(tag);
      ArrayList<byte[]> blocks = new ArrayList<>();
      byte[] bytes = new byte[MAX_BYTE_LENGTH];
      byte[] uriBytes = URI.getBytes();
      int count = 0;
      for (int i = 0; i < uriBytes.length; i++)
      {
        if (count < MAX_BYTE_LENGTH)
        {
          bytes[count] = uriBytes[i];
        }
        else
        {
          blocks.add(bytes);
          bytes = new byte[MAX_BYTE_LENGTH];
          count = 0;
          bytes[count] = uriBytes[i];
        }
        if (i == uriBytes.length - 1 && count != 0)
        {
          blocks.add(bytes);
        }
        count++;
      }
      Log.d("Spotify", blocks.toString());
      try
      {
        card.connect();
        if (card.authenticateSectorWithKeyA(1, MifareClassic.KEY_DEFAULT))
        {
          //Write with block A key
          for (int i = card.sectorToBlock(1); i < blocks.size() + card.sectorToBlock(1); i++)
          {
            Log.d("Spotify",
                    "Writing to block " + i + " : " + blocks.get(i - card.sectorToBlock(1)).length);
            card.writeBlock(i, blocks.get(i - card.sectorToBlock(1)));
          }
          Log.d("Spotify", "Wrote to block A");
          Toast.makeText(this, "Successfully wrote to block 5 with key A", Toast.LENGTH_SHORT)
                  .show();
        }
        else if (card.authenticateSectorWithKeyB(1, MifareClassic.KEY_DEFAULT))
        {
          //Write with block B key
          for (int i = card.sectorToBlock(1); i < blocks.size() + card.sectorToBlock(1); i++)
          {
            Log.d("Spotify",
                    "Writing to block " + i + " : " + blocks.get(i - card.sectorToBlock(1)).length);
            card.writeBlock(i, blocks.get(i - card.sectorToBlock(1)));
          }
          Log.d("Spotify", "Wrote to block B");
          Toast.makeText(this, "Successfully wrote to block 5 with key B", Toast.LENGTH_SHORT)
                  .show();
        }
        else
        {
          Log.e("Spotify", "Failed Auth");
        }
        card.close();
      }
      catch (IOException e)
      {
        Toast.makeText(this, e.getMessage(),
                Toast.LENGTH_SHORT).show();
        Log.e("Spotify", e.getLocalizedMessage());
        e.printStackTrace();
      }
      //StartSearchActivity();
    }
  }

  private void StartSearchActivity()
  {
    Intent searchIntent = new Intent(NFCWriterActivity.this, SpotifyInput.class);
    searchIntent.putExtra("AccessToken", accessToken);
    startActivity(searchIntent);
  }
}