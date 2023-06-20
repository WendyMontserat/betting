package com.xbetvsfonbet.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseNewActivity extends AppCompatActivity {

    TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_new);

        tvScore = findViewById(R.id.tv_score);

        ImageView imageView = findViewById(R.id.iv_basketball);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBet(GameType.Basketball);
            }
        });
        imageView = findViewById(R.id.iv_volleyball);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBet(GameType.Volleyball);
            }
        });
        imageView = findViewById(R.id.iv_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        TextView privacyPolicySim = (TextView) findViewById(R.id.pololo);
        privacyPolicySim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fomo("https://sites.google.com/view/1betvsfnbet");
            }
        });
    }

    private void fomo(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    @Override
    protected void onStart() {
        super.onStart();

        tvScore.setText(getString(R.string.totalScore, AppApplication.getTotalScore()));
    }

    private void startBet(GameType gameType) {
        int firstTeam = AppApplication.getRandom(TeamType.values().length);
        int secondTeam = AppApplication.getRandom(TeamType.values().length, firstTeam);
        AppApplication.setFirstTeam(TeamType.values()[firstTeam]);
        AppApplication.setSecondTeam(TeamType.values()[secondTeam]);

        Intent intent = BetActivity.getStartIntent(this, gameType);
        startActivity(intent);
    }
}