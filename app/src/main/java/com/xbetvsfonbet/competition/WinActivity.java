package com.xbetvsfonbet.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    private static final String PARAM_WIN_TEAM = "win_team";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_win_portrait);

        ImageButton cards = (ImageButton) findViewById(R.id.back_menu);
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent art = new Intent(WinActivity.this,ChooseNewActivity.class);
                startActivity(art);
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        TeamType winTeamType = (TeamType) extras.getSerializable(PARAM_WIN_TEAM);

        TextView textView = findViewById(R.id.tv_winner);
        textView.setText(getString(R.string.winner, winTeamType.name()));

        int bet = AppApplication.getBet();
        TeamType betTeamType = null;
        switch (bet / 100) {
            case 0:
                betTeamType = AppApplication.getFirstTeam();
                break;
            case 1:
                betTeamType = AppApplication.getSecondTeam();
                break;
        }
        int totalScore = AppApplication.getTotalScore();
        if (betTeamType == winTeamType) {
            AppApplication.setTotalScore(totalScore + bet % 100);
        } else {
            AppApplication.setTotalScore(totalScore - bet % 100);
        }
    }

    public static Intent getStartIntent(Context context, TeamType teamType) {
        Bundle extras = new Bundle();
        extras.putSerializable(PARAM_WIN_TEAM, teamType);

        Intent intent = new Intent(context, WinActivity.class);
        intent.putExtras(extras);
        return intent;
    }
}