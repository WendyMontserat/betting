package com.xbetvsfonbet.competition;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class BetActivity extends AppCompatActivity {

    private static final String PARAM_GAME_TYPE = "game_type";

    FrameLayout flFirst10;
    FrameLayout flFirst20;
    FrameLayout flFirst50;
    FrameLayout flSecond10;
    FrameLayout flSecond20;
    FrameLayout flSecond50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bet);

        ImageView imageView = findViewById(R.id.iv_first);
        imageView.setImageResource(getTeamImageResId(AppApplication.getFirstTeam()));
        imageView = findViewById(R.id.iv_second);
        imageView.setImageResource(getTeamImageResId(AppApplication.getSecondTeam()));

        imageView = findViewById(R.id.iv_start);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
        imageView = findViewById(R.id.iv_first_10);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(0, 10);
            }
        });
        imageView = findViewById(R.id.iv_first_20);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(0, 20);
            }
        });
        imageView = findViewById(R.id.iv_first_50);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(0, 50);
            }
        });
        imageView = findViewById(R.id.iv_second_10);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(1, 10);
            }
        });
        imageView = findViewById(R.id.iv_second_20);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(1, 20);
            }
        });
        imageView = findViewById(R.id.iv_second_50);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBet(1, 50);
            }
        });
        flFirst10 = findViewById(R.id.fl_first_10);
        flFirst20 = findViewById(R.id.fl_first_20);
        flFirst50 = findViewById(R.id.fl_first_50);
        flSecond10 = findViewById(R.id.fl_second_10);
        flSecond20 = findViewById(R.id.fl_second_20);
        flSecond50 = findViewById(R.id.fl_second_50);

        AppApplication.setBet(-1);
    }

    private void setBet(int team, int bet) {
        AppApplication.setBet(100 * team + bet);

        flFirst10.setSelected(false);
        flFirst20.setSelected(false);
        flFirst50.setSelected(false);
        flSecond10.setSelected(false);
        flSecond20.setSelected(false);
        flSecond50.setSelected(false);

        switch (team) {
            case 0:
                switch (bet) {
                    case 10:
                        flFirst10.setSelected(true);
                        break;
                    case 20:
                        flFirst20.setSelected(true);
                        break;
                    case 50:
                        flFirst50.setSelected(true);
                        break;
                };
                break;
            case 1:
                switch (bet) {
                    case 10:
                        flSecond10.setSelected(true);
                        break;
                    case 20:
                        flSecond20.setSelected(true);
                        break;
                    case 50:
                        flSecond50.setSelected(true);
                        break;
                };
        }
    }

    private void startGame() {
        if (AppApplication.getBet() > 0) {
            Intent startIntent = null;

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            GameType gameType = (GameType) extras.getSerializable(PARAM_GAME_TYPE);
            switch (gameType) {
                case Basketball:
                    startIntent = new Intent(this, BasketBallActivity.class);
                    break;
                case Volleyball:
                    startIntent = new Intent(this, VolleyBallActivity.class);
                    break;
            }
            startActivity(startIntent);
        }
    }

    private @DrawableRes
    int getTeamImageResId(TeamType teamType) {
        switch (teamType) {
            case Fonbet:
                return R.drawable.team_fonbet;
            case lXbet:
                return R.drawable.team_xbet;
            case Winline:
                return R.drawable.team_winline;
            case Melbet:
                return R.drawable.team_melbet;
        }
        return 0;
    }

    public static Intent getStartIntent(Context context, GameType gameType) {
        Bundle extras = new Bundle();
        extras.putSerializable(PARAM_GAME_TYPE, gameType);

        Intent intent = new Intent(context, BetActivity.class);
        intent.putExtras(extras);
        return intent;
    }
}