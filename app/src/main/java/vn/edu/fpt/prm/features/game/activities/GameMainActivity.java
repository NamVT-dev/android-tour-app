package vn.edu.fpt.prm.features.game.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.Random;

import vn.edu.fpt.prm.R;

public class GameMainActivity extends AppCompatActivity {
    private TextView textViewTime, textViewCount, textViewScore;
    private ImageView balloon1, balloon2, balloon3, balloon4, balloon5, balloon6, balloon7, balloon8, balloon9, balloon10, balloon11, balloon12, balloon13, balloon14, balloon15, balloon16, balloon17, balloon18, balloon19, balloon20, balloon21, balloon22, balloon23, balloon24, balloon25, balloon26, balloon27, balloon28, balloon29, balloon30, balloon31, balloon32, balloon33, balloon34, balloon35, balloon36, balloon37, balloon38, balloon39, balloon40, balloon41, balloon42, balloon43;
    private GridLayout gridLayout;
    int score = 0;

    Runnable runnable;
    Handler handler;

    ImageView[] balloonsArray;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingViews();

        mediaPlayer = MediaPlayer.create(this, R.raw.balloon_pop);

        balloonsArray = new ImageView[]{balloon1, balloon2, balloon3, balloon4, balloon5, balloon6, balloon7, balloon8, balloon9, balloon10, balloon11, balloon12, balloon13, balloon14, balloon15, balloon16, balloon17, balloon18, balloon19, balloon20, balloon21, balloon22, balloon23, balloon24, balloon25, balloon26, balloon27, balloon28, balloon29, balloon30, balloon31, balloon32, balloon33, balloon34, balloon35, balloon36, balloon37, balloon38, balloon39, balloon40, balloon41, balloon42, balloon43};

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                textViewCount.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {

                balloonsControl();

                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long l) {
                        textViewTime.setText("Thời gian còn lại: " + l/1000);
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(GameMainActivity.this, GameResultActivity.class);
                        intent.putExtra("score", score);
                        startActivity(intent);
                        finish();
                    }
                }.start();
            }
        }.start();
    }

    private void bindingViews() {
        textViewTime = findViewById(R.id.textViewTime);
        textViewCount = findViewById(R.id.textViewCount);
        textViewScore = findViewById(R.id.textViewScore);
        balloon1 = findViewById(R.id.balloon1);
        balloon2 = findViewById(R.id.balloon2);
        balloon3 = findViewById(R.id.balloon3);
        balloon4 = findViewById(R.id.balloon4);
        balloon5 = findViewById(R.id.balloon5);
        balloon6 = findViewById(R.id.balloon6);
        balloon7 = findViewById(R.id.balloon7);
        balloon8 = findViewById(R.id.balloon8);
        balloon9 = findViewById(R.id.balloon9);
        balloon10 = findViewById(R.id.balloon10);
        balloon11 = findViewById(R.id.balloon11);
        balloon12 = findViewById(R.id.balloon12);
        balloon13 = findViewById(R.id.balloon13);
        balloon14 = findViewById(R.id.balloon14);
        balloon15 = findViewById(R.id.balloon15);
        balloon16 = findViewById(R.id.balloon16);
        balloon17 = findViewById(R.id.balloon17);
        balloon18 = findViewById(R.id.balloon18);
        balloon19 = findViewById(R.id.balloon19);
        balloon20 = findViewById(R.id.balloon20);
        balloon21 = findViewById(R.id.balloon21);
        balloon22 = findViewById(R.id.balloon22);
        balloon23 = findViewById(R.id.balloon23);
        balloon24 = findViewById(R.id.balloon24);
        balloon25 = findViewById(R.id.balloon25);
        balloon26 = findViewById(R.id.balloon26);
        balloon27 = findViewById(R.id.balloon27);
        balloon28 = findViewById(R.id.balloon28);
        balloon29 = findViewById(R.id.balloon29);
        balloon30 = findViewById(R.id.balloon30);
        balloon31 = findViewById(R.id.balloon31);
        balloon32 = findViewById(R.id.balloon32);
        balloon33 = findViewById(R.id.balloon33);
        balloon34 = findViewById(R.id.balloon34);
        balloon35 = findViewById(R.id.balloon35);
        balloon36 = findViewById(R.id.balloon36);
        balloon37 = findViewById(R.id.balloon37);
        balloon38 = findViewById(R.id.balloon38);
        balloon39 = findViewById(R.id.balloon39);
        balloon40 = findViewById(R.id.balloon40);
        balloon41 = findViewById(R.id.balloon41);
        balloon42 = findViewById(R.id.balloon42);
        balloon43 = findViewById(R.id.balloon43);
        gridLayout = findViewById(R.id.gridLayout);
    }

    public void increaseScore(View view) {
        score++;
        textViewScore.setText("Score: " + score);
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
        mediaPlayer.start();

        for (ImageView balloon: balloonsArray) {
            if (view.getId() == balloon.getId()) {
                balloon.setImageResource(R.drawable.boom);
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void balloonsControl() {
        textViewCount.setVisibility(View.INVISIBLE);
        textViewTime.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView balloon: balloonsArray) {
                    balloon.setVisibility(View.INVISIBLE);
                    balloon.setImageResource(R.drawable.balloon);
                    balloon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            increaseScore(view);
                        }
                    });
                }
                gridLayout.setVisibility(View.VISIBLE);

                Random random = new Random();
                int i = random.nextInt(balloonsArray.length);
                balloonsArray[i].setVisibility(View.VISIBLE);
                int randomTime = 200 + random.nextInt(1001);
                handler.postDelayed(runnable, randomTime);
            }
        };

        handler.post(runnable);
    }
}