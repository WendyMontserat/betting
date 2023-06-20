package com.xbetvsfonbet.competition;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppApplication extends Application {

    private static AppApplication m_instance = null;

    public static AppApplication getInstance() {
        return m_instance;
    }

    private Random m_random;
    private int m_totalScore = 1000;
    private TeamType m_firstTeam = null;
    private TeamType m_secondTeam = null;
    private int m_bet = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        atrLucky();

        m_instance = this;

        m_random = new Random();
    }

    public static int getTotalScore() {
        return m_instance.m_totalScore;
    }

    public static void setTotalScore(int score) {
        m_instance.m_totalScore = score;
    }

    public static TeamType getFirstTeam() {
        return m_instance.m_firstTeam;
    }

    public static void setFirstTeam(TeamType teamType) {
        m_instance.m_firstTeam = teamType;
    }

    public static TeamType getSecondTeam() {
        return m_instance.m_secondTeam;
    }

    public static void setSecondTeam(TeamType teamType) {
        m_instance.m_secondTeam = teamType;
    }

    public static int getBet() {
        return m_instance.m_bet;
    }

    public static void setBet(int bet) {
        m_instance.m_bet = bet;
    }

    public static int getRandom(int count, int... exclude) {
        List<Integer> exs = new ArrayList<>(exclude.length);
        for (int ex : exclude) {
            exs.add(ex);
        }
        Collections.sort(exs, new Comparator<Integer>(){
            @Override
            public int compare(Integer i0, Integer i1) {
                return Integer.compare(i0, i1);
            }
        });
        int random = m_instance.m_random.nextInt(count - exs.size());
        for (int ex : exs) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }
    private void atrLucky() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                JSONObject json = new JSONObject(conversionData);
                write_key1(json.toString());
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                JSONObject json = new JSONObject();
                try {
                    json.put("error", errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                write_key1(json.toString());
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init("8ia3iVZaS4AV949WF4ruFU", conversionListener, this); //todo вставить ключ апса
        AppsFlyerLib.getInstance().start(this);
    }

    public void write_key1(String s) {
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("key1", s);
        editor.apply();
    }
}
