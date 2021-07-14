package com.tunarGames.footballswitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH = "switch";
    public static final String SWITCHTWO = "switch2";

    static MediaPlayer musicPlayer;
    static MediaPlayer buttonPlayer;
    static boolean isMusicOn = true;
    static boolean isVibrationOn = true;
    static int alphaSound, alphaVibrate;
    static int length;


    int continueMusic, bestScore = 0;

    SharedPreferences pf;
    Vibrator v;

    TextView scoreTextView;
    Button playButton;
    Button soundButton;
    Button vibrationButton;

    ImageView footballImageView;
    ImageView circleImageView;

    boolean isSoundOn = true;
    boolean soundCheck;
    boolean vibrationCheck;
    boolean isVibrating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        isVibrationOn = vibrationCheck;
        isMusicOn = soundCheck;
/*
        if (isMusicOn){
            alphaSound = 128;
        } else {
            alphaSound = 255;
        }
        if (isVibrationOn){
            alphaVibrate = 128;
        } else {
            alphaVibrate = 255;
        }
        */

        playButton = findViewById(R.id.playButton);
        soundButton = findViewById(R.id.soundButton);
        vibrationButton = findViewById(R.id.vibrationButton);
        footballImageView = findViewById(R.id.footballImageView);
        circleImageView = findViewById(R.id.circleImageView);
        scoreTextView = findViewById(R.id.scoreTextView);

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1500);
        rotate.setRepeatCount(Animation.INFINITE);
        circleImageView.startAnimation(rotate);

        RotateAnimation ballrotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        ballrotate.setDuration(5000);
        ballrotate.setInterpolator(new LinearInterpolator());
        ballrotate.setRepeatCount(Animation.INFINITE);
        footballImageView.startAnimation(ballrotate);

        musicPlayer = MediaPlayer.create(MainActivity.this, R.raw.background);
        buttonPlayer = MediaPlayer.create(MainActivity.this, R.raw.button);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        pf = this.getSharedPreferences(
                "com.first.footballswitch", Context.MODE_PRIVATE);

        bestScore = pf.getInt("score", bestScore);
        scoreTextView.setText("BEST \n" + bestScore);

        if (isMusicOn) {
            musicPlayer.start();
            musicPlayer.setLooping(true);
            pf.edit().putBoolean("music", true).apply();
            isMusicOn = pf.getBoolean("music", isMusicOn);
            ///Opacity of Sound Button
            pf.edit().putInt("alphasound", 255).apply();
            alphaSound = pf.getInt("alphasound", alphaSound);
            soundButton.getBackground().setAlpha(alphaSound);
        } else {
            soundButton.getBackground().setAlpha(128);
        }
        if (isVibrationOn) {
            ///Opacity of Vibrate Button
            pf.edit().putInt("alphavibrate", 255).apply();
            alphaVibrate = pf.getInt("alphavibrate", alphaVibrate);
            vibrationButton.getBackground().setAlpha(alphaVibrate);
        } else {
            vibrationButton.getBackground().setAlpha(128);
        }
    }

    public void playGame(View view) {
        if (isVibrationOn) {
            startVibrate();
        }
        if (isMusicOn) {
            continueMusic = musicPlayer.getCurrentPosition();
            buttonPlayer.start();
        }
        Intent startGame = new Intent(MainActivity.this, StartGame.class);
        startGame.putExtra("vibration", isVibrationOn);
        startGame.putExtra("music", isMusicOn);
        startGame.putExtra("length", continueMusic);
        startActivity(startGame);
    }

    public void soundSetting(View view) {
        if (isMusicOn) {
            if (isVibrationOn) {
                startVibrate();
            }
            isMusicOn = false;
            buttonPlayer.start();
            musicPlayer.pause();
            length = musicPlayer.getCurrentPosition();
            buttonPlayer.pause();
            alphaSound = 128;
            soundButton.getBackground().setAlpha(alphaSound);
            pf.edit().putInt("alphasound", alphaSound).apply();
            pf.edit().putBoolean("music", isMusicOn).apply();
        } else {
            if (isVibrationOn) {
                startVibrate();
            }
            isMusicOn = true;
            alphaSound = 255;
            soundButton.getBackground().setAlpha(alphaSound);
            musicPlayer.seekTo(length);
            musicPlayer.start();
            pf.edit().putBoolean("music", isMusicOn).apply();
            pf.edit().putInt("alphasound", alphaSound).apply();
        }
    }

    public void vibrateSetting(View view) {
        if (isVibrationOn) {
            if (isMusicOn) {
                buttonPlayer.start();
            }
            stopVibrate();
            alphaVibrate = 128;
            vibrationButton.getBackground().setAlpha(alphaVibrate);
            pf.edit().putInt("alphavibrate", alphaVibrate).apply();
            isVibrationOn = false;
            pf.edit().putBoolean("vibrate", isVibrationOn).apply();
        } else {
            if (isMusicOn) {
                buttonPlayer.start();
            }
            startVibrate();
            alphaVibrate = 255;
            vibrationButton.getBackground().setAlpha(alphaVibrate);
            pf.edit().putInt("alphavibrate", alphaVibrate).apply();
            isVibrationOn = true;
            pf.edit().putBoolean("vibrate", isVibrationOn).apply();
        }
    }

    public void startVibrate() {
        // Vibrate for 100 milliseconds
        assert v != null;
        v.vibrate(100);
    }

    public void stopVibrate() {
        assert v != null;
        v.cancel();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        buttonPlayer.stop();
        isSoundOn = isMusicOn;
        isVibrating = isVibrationOn;
        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isMusicOn) {
            length = musicPlayer.getCurrentPosition();
            musicPlayer.pause();
        }
        isSoundOn = isMusicOn;
        isVibrating = isVibrationOn;
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isMusicOn) {
            musicPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicOn) {
            musicPlayer.seekTo(length);
            musicPlayer.start();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH, isSoundOn);
        editor.putBoolean(SWITCHTWO, isVibrating);
        editor.apply();
        Log.e("asdad:", "Sound Saved As: " + isSoundOn);
        Log.e("asdad:", "Vibration Saved As: " + isVibrating);

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        soundCheck = sharedPreferences.getBoolean(SWITCH, true);
        vibrationCheck = sharedPreferences.getBoolean(SWITCHTWO, true);
        Log.e("asdad:", "Sound Loaded As: " + soundCheck);
        Log.e("asdad:", "Vibration Loaded As: " + vibrationCheck);

    }

}
