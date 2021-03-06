package com.udem.videotracker.api;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.udem.videotracker.R;
import android.os.Bundle;
import android.widget.Toast;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener{
 
 public static final String API_KEY = "AIzaSyBa6hEa_85xTgWs8G-uf-rqyw_4X-RnMHU";
 public static final String VIDEO_ID = "o7VVHhK9zf0";
 public String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        url = getIntent().getStringExtra("url");
        
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
        youTubePlayerView.initialize(API_KEY, this);
    }

 @Override
 public void onInitializationFailure(Provider provider,
   YouTubeInitializationResult result) {
  Toast.makeText(getApplicationContext(), 
    "onInitializationFailure()", 
    Toast.LENGTH_LONG).show();
 }

 @Override
 public void onInitializationSuccess(Provider provider, YouTubePlayer player,
   boolean wasRestored) {
  if (!wasRestored) {
        player.cueVideo(url);
      }
 }

}