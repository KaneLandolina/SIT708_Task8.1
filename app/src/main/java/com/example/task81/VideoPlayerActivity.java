package com.example.task81;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.task81.databinding.ActivitySignUpBinding;
import com.example.task81.databinding.ActivityVideoPlayerBinding;

public class VideoPlayerActivity extends AppCompatActivity {

    ActivityVideoPlayerBinding binding;

    ExoPlayer exoPlayer;

    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating binding for using items from view
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            videoUrl = bundle.getString("videoUrl");
            Log.v("received", videoUrl);
        }

        exoPlayer = new ExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(exoPlayer);

        //MediaItem mediaItem = MediaItem.fromUri("https://www.youtube.com/watch?v=" + videoUrl + "&el=info&ps=default&eurl=&gl=US&hl=en");
        //MediaItem mediaItem = MediaItem.fromUri(Uri.parse("https://www.youtube.com/embed/" + videoUrl));
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("https://www.youtube.com/watch?v=" + videoUrl));
        //MediaItem mediaItem1 = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4");
        exoPlayer.setMediaItem(mediaItem);
        //exoPlayer.addMediaItem(mediaItem1);
        exoPlayer.prepare();
        exoPlayer.play();



    }
}