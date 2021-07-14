package com.tunarGames.footballswitch;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartGame extends AppCompatActivity {
    static MediaPlayer clickPlayer;
    //Game Play Variables
    final Random rnd = new Random();
    //ALL USED VARIABLES
    ImageView pauseButton, closeButton, pauseBackground, handImageView;
    Button restartButton, menuButton, resumeButton;
    LottieAnimationView blastAnimation;
    boolean isMusicOn;
    boolean isVibrationOn;
    Vibrator v;
    int length, bestScore = 0;
    MediaPlayer musicPlayer, buttonPlayer, coinPlayer, blastPlayer;
    SharedPreferences pf;
    ImageView football, centerCoin, centerCoin2;
    ImageView touchField, topIntersectionImg, colorChanger, colorChanger2;
    ImageView imageView0, imageView1, imageView2, imageView3, imageViewkk;
    ImageView bottomLineRing1;
    ImageView bottomLine, bottomLineOf2ndRing, topLineOf2ndRing;
    GridLayout ring1, ring2;
    FrameLayout frameLayout, frameLayout2;
    TextView textView;
    TextView score;
    Timer timer;

    DisplayMetrics metrics;

    boolean gameOver = false;
    boolean repeatBottom = false;

    double screenWidth;
    double screenHeight;

    int count = 0;
    int counter = 0;
    int count3 = 0;
    int count2 = 0;
    int count0 = 0;
    int count1 = 0;
    int count6 = 0;
    int count5 = 0;
    int count7 = 0;
    int count8 = 0;
    int scoreCount = 0;
    int currentFootball;
    int imageChanged = 0;
    int bottomRepeatCount = 0;

    private boolean timerRunning = false;
    //BROADCAST RECEIVER USED TO HANDLE ANIMATIONS AND ACTIONS OUTSIDE THE TIMER
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (Objects.equals(intent.getAction(), "stop")) {

                stopFootball();

            } else if (Objects.equals(intent.getAction(), "stopFootball")) {
                //football.animate().cancel();
                football.animate().translationYBy(1000).setDuration(1000);
                frameLayout.animate().translationYBy(100).setDuration(250);
                frameLayout2.animate().translationYBy(100).setDuration(250);
                colorChanger.animate().translationYBy(100).setDuration(250);
                colorChanger2.animate().translationYBy(100).setDuration(250);
            } else if (Objects.equals(intent.getAction(), "gameOver")) {
                gameOver();
            } else if (Objects.equals(intent.getAction(), "repeatRing")) {
                Log.e("asdasd", "//////////////////Repeated ring");
                frameLayout.animate().cancel();
                frameLayout.setY((float) ((-1500 / 2.5) * metrics.scaledDensity));
                count5 = 0;
            } else if (Objects.equals(intent.getAction(), "repeatColorChanger")) {
                colorChanger2.animate().cancel();
                colorChanger2.setY((float) ((-1500 / 2.5) * metrics.scaledDensity));
            } else if (Objects.equals(intent.getAction(), "repeatSecondRing")) {
                frameLayout2.animate().cancel();
                frameLayout2.setY((float) ((-1500 / 2.5) * metrics.scaledDensity));
                count6 = 0;
                count5 = 0;
            } else if (Objects.equals(intent.getAction(), "repeatColorChanger2")) {
                colorChanger.animate().cancel();
                colorChanger.setY((float) ((-1500 / 2.5) * metrics.scaledDensity));
            } else if (Objects.equals(intent.getAction(), "repeatLayout")) {
                Log.e("asda", "/////////////////FrameLayout should move");
                frameLayout.animate().cancel();
                frameLayout.setY((float) ((-1500 / 2.5) * metrics.scaledDensity));
            } else if (Objects.equals(intent.getAction(), "changeColor")) {
                int rand = rnd.nextInt(3);
                while (rand == currentFootball) {
                    rand = rnd.nextInt(3);
                }

                final String str = "football" + rand;
                currentFootball = rand;
                football.setImageDrawable(
                        getResources().getDrawable(getResourceID(str, "drawable",
                                getApplicationContext())));
            } else if (Objects.equals(intent.getAction(), "coinReceived")) {
                Log.e("asda", "/////////////////CoinReceived");
                centerCoin.animate().cancel();
                centerCoin.animate().translationYBy(-400).alpha(0).setDuration(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centerCoin.animate().translationYBy(400).alpha(1);
                    }
                }, 8 * 1000);
                score.setText(Integer.toString(scoreCount));
            } else if (Objects.equals(intent.getAction(), "secondCoinReceived")) {
                Log.e("asda", "/////////////////CoinReceived");
                centerCoin2.animate().cancel();
                centerCoin2.animate().translationYBy(-400).alpha(0).setDuration(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        centerCoin2.animate().translationYBy(400).alpha(1);
                    }
                }, 8 * 1000);
                score.setText(Integer.toString(scoreCount));
            } else if (Objects.equals(intent.getAction(), "stopFootballAtBottom")) {
                football.animate().cancel();
                football.animate().translationYBy(-300).setDuration(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomRepeatCount = 0;
                    }
                }, 2 * 1000);
            } else if (Objects.equals(intent.getAction(), "colorChanger")) {
                colorChanger.animate().cancel();
                colorChanger.animate().alpha(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        colorChanger.animate().alpha(1);
                        count7 = 0;
                    }
                }, 5 * 1000);
            } else if (Objects.equals(intent.getAction(), "colorChangerTwo")) {
                colorChanger2.animate().cancel();
                colorChanger2.animate().alpha(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        colorChanger2.animate().alpha(1);
                        count8 = 0;
                    }
                }, 5 * 1000);
            }
        }
    };

    /// THIS IS USED TO PIC RANDOM FOOTBALL FROM THE DRAWABLE RESOURCES
    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        } else {
            return ResourceID;
        }
    }

    /// MAX AMONG FOUR NUMBERS,, USED TO FIND WHICH IMAGE IS AT THE BOTTOM
    public static int max(int a, int b, int c, int d) {

        int count = 0;
        int max = a;

        if (b > max) {
            max = b;
            count = 1;
        }
        if (c > max) {
            max = c;
            count = 2;
        }
        if (d > max) {
            max = d;
            count = 3;
        }
        return count;
    }

    private void stopFootball() {
        Log.i("ASdasd", "Broadcast Game Over");

        // pf.edit ().putInt ("score", scoreCount).apply ();
        bestScore = pf.getInt("score", scoreCount);
        if (scoreCount > bestScore) {
            pf.edit().putInt("score", scoreCount).apply();
        } else {
            pf.edit().putInt("score", bestScore).apply();
        }

        blastAnimation.setVisibility(View.VISIBLE);

        if (isMusicOn) {
            blastPlayer.start();
        }

        blastAnimation.playAnimation();
        pauseButton.setVisibility(View.INVISIBLE);
        //  textView.setAlpha(1);
        football.animate().alpha(0);
        frameLayout.animate().cancel();
        frameLayout2.animate().cancel();
        colorChanger.animate().cancel();
        colorChanger2.animate().cancel();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                gameOverAnim();


            }
        }, 1500);

    }

    public void gameOverAnim() {
        blastAnimation.setVisibility(View.INVISIBLE);
        pauseBackground.setVisibility(View.VISIBLE);
        textView.setAlpha(1);
        closeButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        count1 = 0;
        castViews();
        pf = this.getSharedPreferences(
                "com.first.footballswitch", Context.MODE_PRIVATE);

        textView.setText("Game Over !!!");

        musicPlayer = MediaPlayer.create(this, R.raw.background);
        buttonPlayer = MediaPlayer.create(this, R.raw.button);
        coinPlayer = MediaPlayer.create(this, R.raw.money);
        blastPlayer = MediaPlayer.create(this, R.raw.dead);
        clickPlayer = MediaPlayer.create(this, R.raw.jump);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Intent intent = getIntent ();
        isVibrationOn = getIntent().getExtras().getBoolean("vibration");
        isMusicOn = getIntent().getExtras().getBoolean("music");
        length = getIntent().getExtras().getInt("length");

        if (isMusicOn) {
            musicPlayer.seekTo(length);
            musicPlayer.start();
            musicPlayer.setLooping(true);
        }

        // DEVICE METRICS USED TO IDENTIFY DEVICE DIMENSIONS
        metrics = getResources().getDisplayMetrics();


        //RANDOM FOOTBALL IS SET AMONG FOUR IN THE RESOURCES,, GREEN, WHITE, YELLOW, RED
        int random = rnd.nextInt(3);
        currentFootball = random;
        final String str = "football" + random;

        football.setImageDrawable(
                getResources().getDrawable(getResourceID(str, "drawable",
                        getApplicationContext()))
        );


        animateCenterCoin();


        timer = new Timer();


        //Rings Rotation
        rotateRings();


        //imageBar.animate().rotation(-225).setDuration(1000);
        score.setText(Integer.toString(scoreCount));

        //Broadcast Receiver filters,
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("stop");
        intentFilter.addAction("stopFootball");
        intentFilter.addAction("gameOver");
        intentFilter.addAction("repeatRing");
        intentFilter.addAction("repeatColorChanger");
        intentFilter.addAction("repeatSecondRing");
        intentFilter.addAction("repeatColorChanger2");
        intentFilter.addAction("repeatLayout");
        intentFilter.addAction("changeColor");
        intentFilter.addAction("coinReceived");
        intentFilter.addAction("secondCoinReceived");
        intentFilter.addAction("stopFootballAtBottom");
        intentFilter.addAction("colorChanger");
        intentFilter.addAction("colorChangerTwo");


        registerReceiver(broadcastReceiver, intentFilter);

        //Delay given,, So that the Main Activity loads Completely
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTimeOnEachMin();
            }
        }, 500);

        //Device Dimensions
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        touchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handImageView.setVisibility(View.INVISIBLE);
                int y1 = getCoordinateY(imageViewkk);
                int y = getCoordinateY(football);
                double distance = Math.sqrt((y - y1) ^ 2);
                if (isVibrationOn) {
                    startVibrate();
                }
                if (isMusicOn) {
                    clickPlayer.start();
                }
                if (distance > 0 && distance < 1 || Double.isNaN(distance)) {
                    // Football Over Center,  Layout Moves
                    frameLayout.animate().translationYBy(200).setDuration(250);
                    frameLayout2.animate().translationYBy(200).setDuration(250);
                    colorChanger2.animate().translationYBy(200).setDuration(250);
                    colorChanger.animate().translationYBy(200).setDuration(250);

                } else {
                    // Football Below Center,, Football Moves
                    football.animate().translationYBy(-100).rotationBy(90).setDuration(125);
                }


                // Football Falls After Moving Up animation is Over
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        football.animate().translationYBy(5000).rotationBy(5 * 360).setDuration(4 * 1000);

                    }
                }, 125);
            }
        });
    }

    public void startVibrate() {
        // Vibrate for 100 milliseconds
        assert v != null;
        v.vibrate(100);
    }

    private void rotateRings() {
        ring1.animate().rotation(10000).setDuration(90 * 1000);
        ring2.animate().rotation(10000).setDuration(90 * 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateRings();
            }
        }, 90 * 1000);
    }

    private void castViews() {

        //Views Casted to the Activity

        ring1 = findViewById(R.id.image_bar);
        ring2 = findViewById(R.id.image_bar2);
        football = findViewById(R.id.football);
        frameLayout = findViewById(R.id.image_container);
        frameLayout2 = findViewById(R.id.image_container2);
        imageView0 = findViewById(R.id.image1);
        imageView1 = findViewById(R.id.image2);
        imageView2 = findViewById(R.id.image3);
        centerCoin = findViewById(R.id.image_coin);
        centerCoin2 = findViewById(R.id.image_coin2);
        imageView3 = findViewById(R.id.image4);
        imageView0 = findViewById(R.id.image1);
        score = findViewById(R.id.tv_score);
        imageView1 = findViewById(R.id.image2);
        bottomLineOf2ndRing = findViewById(R.id.bottom_Intersection_img22);
        bottomLineRing1 = findViewById(R.id.bottom_Intersection_img2);
        topLineOf2ndRing = findViewById(R.id.top_Intersection_img2);
        imageView2 = findViewById(R.id.image3);
        imageView3 = findViewById(R.id.image4);
        bottomLine = findViewById(R.id.bottom);
        touchField = findViewById(R.id.touch_Field);
        imageViewkk = findViewById(R.id.center);
        textView = findViewById(R.id.text);
        colorChanger = findViewById(R.id.color_Changer);
        colorChanger2 = findViewById(R.id.color_Changer2);
        topIntersectionImg = findViewById(R.id.top_Intersection_img);

        pauseButton = findViewById(R.id.pauseImageView);
        pauseBackground = findViewById(R.id.pauseBackground);
        menuButton = findViewById(R.id.menuButton);
        resumeButton = findViewById(R.id.resumeButton);
        restartButton = findViewById(R.id.restartButton);
        closeButton = findViewById(R.id.closeImageView);
        blastAnimation = findViewById(R.id.blastAnimation);
        handImageView = findViewById(R.id.handImageView);
    }

    private void checkIntersection() {

        // Any Intersection is Detected here...

        //Color Changers Detected
        checkChangeColor(colorChanger2);
        checkChangeColor(colorChanger);
        checkColorChanger1();
        checkColorChanger2();

        checkCoinIntersection();
        checkSecondCoinIntersection();


        //Ring Intersection is checked,,, Switching the image having the highest y coordinate so that it is at the bottom
        switch (max(getCoordinateY(imageView0), getCoordinateY(imageView1),
                getCoordinateY(imageView2), getCoordinateY(imageView3))) {
            case 0:

                // Once we Know which image is at bottom, opposite to that is at the top.
                // As both rings move at same frequency, both have same top and bottom images.

                checkRingIntersection(bottomLineRing1, 0);
                checkRingIntersection(topIntersectionImg, 2);
                checkRingIntersection(bottomLineOf2ndRing, 0);
                checkRingIntersection(topLineOf2ndRing, 2);

                break;
            case 1:
                checkRingIntersection(bottomLineRing1, 3);
                checkRingIntersection(topIntersectionImg, 1);
                checkRingIntersection(bottomLineOf2ndRing, 3);
                checkRingIntersection(topLineOf2ndRing, 1);
                break;
            case 2:
                checkRingIntersection(bottomLineRing1, 1);
                checkRingIntersection(topIntersectionImg, 3);
                checkRingIntersection(bottomLineOf2ndRing, 1);
                checkRingIntersection(topLineOf2ndRing, 3);
                break;
            case 3:
                checkRingIntersection(bottomLineRing1, 2);
                checkRingIntersection(topIntersectionImg, 0);
                checkRingIntersection(bottomLineOf2ndRing, 2);
                checkRingIntersection(topLineOf2ndRing, 0);
                break;
        }
    }

    private void checkColorChanger2() {
        if (isViewOverlapping(football, colorChanger2)) {
            if (count7 == 0) {
                sendBroadcast(new Intent("colorChangerTwo"));
                count7++;
            }
        }
    }

    private void checkColorChanger1() {
        if (isViewOverlapping(football, colorChanger)) {
            if (count8 == 0) {
                sendBroadcast(new Intent("colorChanger"));
                count8++;
            }
        }
    }

    private void checkSecondCoinIntersection() {
        // second Coin Intersection
        if (isIntersecting(football, centerCoin2)) {
            //if (isViewOverlapping(football, centerCoin2)) {
            if (count6 == 0) {
                sendBroadcast(new Intent("secondCoinReceived"));
                scoreCount++;
                count6++;
            }
        }
    }

    private void checkCoinIntersection() {
        // first Coin Intersection
        if (isIntersecting(football, centerCoin)) {
            //  if (isViewOverlapping(football, centerCoin)) {
            Log.e("ASDasd", "///////////////////Coin Overlapped");
            if (count5 == 0) {
                sendBroadcast(new Intent("coinReceived"));
                scoreCount++;
                count5++;
            }
        }
    }

    private void checkChangeColor(ImageView colorChanger2) {
        // color changer intersection
        if (isViewOverlapping(football, colorChanger2)) {
            if (imageChanged == 0) {
                sendBroadcast(new Intent("changeColor"));
                imageChanged++;
            }
        }
    }

    private int getCoordinateY(ImageView imageView) {
        // Returns Y coordinate of provided ImageView
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        return location[1];
    }

    private boolean isIntersecting(ImageView imageView1, ImageView imageView2) {
        int y = getCoordinateY(imageView1);
        int y1 = getCoordinateY(imageView2);

        double distance = Math.sqrt((y1 - y) ^ 2);

        if (Double.isNaN(distance)) {
            distance = Math.sqrt((y - y1) ^ 2);
        }
        return distance > 0 && distance < 6;
    }

    private void checkRingIntersection(ImageView imageView, int i) {
        // ring intersection detection here
        int y = getCoordinateY(imageView);
        int y1 = getCoordinateY(football);

        double distance = Math.sqrt((y1 - y) ^ 2);

        if (Double.isNaN(distance)) {
            distance = Math.sqrt((y - y1) ^ 2);
        }


        if (distance > 0 && distance < 6) {
            switch (i) {
                case 2:
                    if (currentFootball != i) {
                        Log.i("asdad", "////////////////Image 3 Game Over");
                        gameOver();
                    } else {
                        Log.i("ASda", "////////////football rings match");
                    }
                    break;
                case 1:
                    if (currentFootball != i) {
                        Log.i("asdad", "////////////////Image 2 Game Over");
                        gameOver();
                    } else {
                        Log.i("ASda", "////////////football rings match");
                    }
                    break;
                case 3:
                    if (currentFootball != i) {
                        Log.i("asdad", "////////////////Image 1 Game Over");
                        gameOver();
                    } else {
                        Log.i("ASda", "////////////football rings match");
                    }
                    break;
                case 0:
                    // Log.i("asdad", "Image 0 up");
                    if (currentFootball != i) {
                        Log.i("asdad", "////////////////Image 0 Game Over");
                        gameOver();
                    } else {
                        Log.i("ASda", "////////////football rings match");
                    }
                    break;
            }

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void gameOver() {
        Log.i("ASda", "football rings doNot match");
        touchField.setOnClickListener(null);
        timer.cancel();
        timerRunning = false;

        if (count == 0) {
            sendBroadcast(new Intent("stop"));
            count++;
        }

        //football.setOnTouchListener(null);

        gameOver = true;
    }

    //Pause Game
    public void pauseGame(View view) {
        if (isMusicOn) {
            buttonPlayer.start();
        }
        if (isVibrationOn) {
            startVibrate();
        }
        pauseBackground.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        textView.setText("Game Paused");
        textView.setAlpha(1);
        touchField.setEnabled(false);
        football.animate().cancel();
    }

    public void restartGame(View view) {
        if (isMusicOn) {
            buttonPlayer.start();
        }
        if (isVibrationOn) {
            startVibrate();
        }
        overridePendingTransition(0, 0);
        recreate();
        overridePendingTransition(0, 0);
    }

    public void goToMenu(View view) {
        if (isMusicOn) {
            buttonPlayer.start();
            musicPlayer.pause();
        }
        if (isVibrationOn) {
            startVibrate();
        }
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
        finish();
    }

    public void closeMenu(View view) {
        if (isMusicOn) {
            buttonPlayer.start();
        }
        if (isVibrationOn) {
            startVibrate();
        }
        pauseBackground.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
        menuButton.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        textView.setAlpha(0);
        textView.setText("Game Over !!!");
        touchField.setEnabled(true);
    }

    public void returnGame(View v) {
        if (isMusicOn) {
            buttonPlayer.start();
        }
        if (isVibrationOn) {
            startVibrate();
        }
        pauseBackground.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
        menuButton.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        textView.setAlpha(0);
        textView.setText("Game Over !!!");
        touchField.setEnabled(true);
    }

    private void animateCenterCoin() {
        centerCoin.animate().rotationBy(10000).setDuration(60 * 1000);
        centerCoin2.animate().rotationBy(10000).setDuration(60 * 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateCenterCoin();
            }
        }, 60 * 1000);

    }
    //Timer is the base of the Game Play,, it Detects Any Action Change in the game play
    // We can say Time is the mother of Game Play

    private boolean isViewOverlapping(View firstView, View secondView) {

        final int[] location = new int[2];
        firstView.getLocationInWindow(location);
        Rect rect1 = new Rect(location[0], location[1], location[0] + firstView.getWidth(), location[1] + firstView.getHeight());
        secondView.getLocationInWindow(location);
        Rect rect2 = new Rect(location[0], location[1], location[0] + secondView.getWidth(), location[1] + secondView.getHeight());
        return rect1.intersect(rect2);
    }

    public void updateTimeOnEachMin() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int y1 = getCoordinateY(imageViewkk);
                int y = getCoordinateY(football);
                double distance = Math.sqrt((y - y1) ^ 2);
                if (distance > 0 && distance < 1 || Double.isNaN(distance)) {
                    if (counter == 0) {
                        sendBroadcast(new Intent("stopFootball"));
                        counter++;
                    }
                } else {
                    counter = 0;
                }
                checkIntersection();

                checkBottomIntersection();

                checkRepeatIntersection();
                if (timerRunning) {
                    try {
                        checkIntersection();
                    } catch (Exception e) {
                        Log.e("Asdasd", "Exception: " + e);
                    }
                }
            }
        }, 0, 23);

    }

    private void checkRepeatIntersection() {
        //Any View Goes Off Screen it gets Repeated

        if (frameLayout.getY() > screenHeight) {
            if (count0 == 0) {
                Log.e("Asdasd", "////////////////// BroadCast Sent");
                sendBroadcast(new Intent("repeatLayout"));
                count0++;
                count1 = 0;
            }
        }
        if (colorChanger2.getY() > screenHeight) {

            if (count1 == 0) {
                Log.e("Asdasd", "////////////////// BroadCast Sent");
                sendBroadcast(new Intent("repeatColorChanger"));
                count1++;
                count2 = 0;
                imageChanged = 0;
            }
        }
        if (frameLayout2.getY() > screenHeight) {
            if (count2 == 0) {
                Log.e("Asdasd", "////////////////// BroadCast Sent");
                sendBroadcast(new Intent("repeatSecondRing"));
                count2++;
                count3 = 0;
            }
        }
        if (colorChanger.getY() > screenHeight) {
            if (count3 == 0) {
                Log.e("Asdasd", "////////////////// BroadCast Sent");
                sendBroadcast(new Intent("repeatColorChanger2"));
                count3++;
                count0 = 0;
                imageChanged = 0;
            }
        }
    }

    private void checkBottomIntersection() {

        //Bottom Line is used When user does not click and football goes off screen,, so the game is over

        int y = getCoordinateY(bottomLine);
        int y1 = getCoordinateY(football);
        double distance = Math.sqrt((y - y1) ^ 2);
        if (distance > 0 && distance < 1 || Double.isNaN(distance)) {
            if (!repeatBottom) {
                sendBroadcast(new Intent("gameOver"));
                repeatBottom = true;
            }
        } else {
            repeatBottom = false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
        finishAffinity();
    }

    // When Activity is destroyed, Broadcast Receiver gets cancelled along with the timer
    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        timer.cancel();
        musicPlayer.stop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isMusicOn) {
            length = musicPlayer.getCurrentPosition();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (isMusicOn) {
            musicPlayer.pause();
        }
    }
}
