package com.teamRTL.cloudmedicalproject.UIs.patients;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityShowDetailsArticleBinding;


//هان بنعرض التفاصيل للمريض والفيديو
public class ShowDetailsArticleActivity extends AppCompatActivity {
    String url;
    PlayerView playerView;
    SimpleExoPlayer player;
    int currentWindow = 0;
    boolean playWhenReady = true;
    long playBackPosition = 0;
    ActivityShowDetailsArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowDetailsArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        url = getIntent().getStringExtra("url");
        playerView = findViewById(R.id.video_view1);
        getData();

    }

    private void getData() {

        if (getIntent().getExtras() != null) {
            String name = getIntent().getStringExtra("name");
            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");
            String image1 = getIntent().getStringExtra("logo");
            url = getIntent().getStringExtra("video");

            binding.doctorName.setText(name);
            binding.description.setText(description);
            binding.name.setText(title);


            Glide.with(this).load(image1).placeholder(R.drawable.ic_launcher_background).into(binding.userImage);

        }
    }
    @Override
    protected void onStart() {
        initVideo();
        super.onStart();

    }

    public void initVideo() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri uri = Uri.parse(url);
        DataSource.Factory dataSource = new DefaultDataSourceFactory(this, "video");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSource).createMediaSource(MediaItem.fromUri(uri));

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playBackPosition);
        player.prepare(mediaSource, false, false);

    }

    public void realiseVideo() {
        if(player != null){
            playWhenReady = player.getPlayWhenReady();
            playBackPosition = player.getContentPosition();
            currentWindow = player.getCurrentWindowIndex();

            player.release();
            player = null;
        }
    }


    @Override
    protected void onPause() {
        realiseVideo();
        super.onPause();
    }

    @Override
    protected void onStop() {
        realiseVideo();
        super.onStop();
    }
}
