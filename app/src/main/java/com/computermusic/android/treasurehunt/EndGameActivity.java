package com.computermusic.android.treasurehunt;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        MediaPlayer triumphMediaPlayer = MediaPlayer.create(this, R.raw.triumph);
        triumphMediaPlayer.start();
    }

}
