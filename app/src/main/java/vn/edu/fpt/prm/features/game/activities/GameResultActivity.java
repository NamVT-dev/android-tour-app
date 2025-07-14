package vn.edu.fpt.prm.features.game.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class GameResultActivity extends AppCompatActivity {

    private TextView textViewInfo, textViewMyScore, textViewHighScore, textViewCoin;
    private Button buttonPlayAgain, buttonQuit;

    int myScore;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingViews();
        bindingActions();

        myScore = getIntent().getIntExtra("score", 0);
        textViewMyScore.setText("Điểm của bạn: " + myScore);

        sharedPreferences = this.getSharedPreferences("score", Context.MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("highScore", 0);
        int coin = sharedPreferences.getInt("coin", 0);
        int receivedCoin = myScore * 100;
        sharedPreferences.edit().putInt("coin", coin + receivedCoin).apply();
        textViewCoin.setText("Xu nhận được: " + receivedCoin);

        if (myScore > highScore) {
            sharedPreferences.edit().putInt("highScore", myScore).apply();
            textViewHighScore.setText("Điểm cao nhất: " + myScore);
            textViewInfo.setText("Chúc mừng. Điểm cao nhất mới");
        } else {
            textViewHighScore.setText("Điểm cao nhất: " + highScore);
        }
    }

    private void bindingViews() {
        textViewHighScore = findViewById(R.id.textViewHighScore);
        textViewInfo = findViewById(R.id.textViewInfo);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        textViewCoin = findViewById(R.id.textViewCoin);
        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonQuit = findViewById(R.id.buttonQuitGame);
    }

    private void bindingActions() {
        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameResultActivity.this, GameMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.goTo(GameResultActivity.this, Screen.HOME);
            }
        });
    }
}