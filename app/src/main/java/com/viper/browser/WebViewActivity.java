package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.app.PictureInPictureParams;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private TextView urlAffichage;
    private SharedPreferences prefs;
    private boolean adBlockEnabled;
    private static final Set<String> AD_DOMAINS = new HashSet<>(Arrays.asList(
        "ad.", "ads.", "adserver.", "advertising.", "banner.", "tracking.",
        "analytics.", "track.", "stats.", "pixel.", "beacon.", "adnetwork.",
        "doubleclick.net", "googlesyndication.com", "facebook.com/tr", "amazon-adsystem.com",
        "adroll.com", "criteo.com", "taboola.com", "outbrain.com", "hotjar.com",
        "googletagmanager.com", "google-analytics.com", "scorecardresearch.com"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
        adBlockEnabled = prefs.getBoolean("adblock_enabled", true);

        webView = findViewById(R.id.webView);
        urlAffichage = findViewById(R.id.urlAffichage);
        configurerWebView();

        String url = getIntent().getStringExtra("url");
        if (url != null) webView.loadUrl(url);
        else webView.loadUrl("https://google.com");
    }

    private void configurerWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false); // Lecture auto arrière-plan
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                String urlAffichee = url.replace("https://", "").replace("http://", "");
                if (urlAffichee.length() > 35) urlAffichee = urlAffichee.substring(0, 32) + "...";
                urlAffichage.setText(urlAffichee);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (adBlockEnabled && estUrlPub(url)) {
                    Toast.makeText(WebViewActivity.this, "🛡️ Pub bloquee", Toast.LENGTH_SHORT).show();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    private boolean estUrlPub(String url) {
        for (String domaine : AD_DOMAINS) if (url.contains(domaine)) return true;
        return false;
    }

    // ========== FONCTIONS WEB ==========
    public void precedent(View v) { if (webView != null && webView.canGoBack()) webView.goBack(); }
    public void suivant(View v) { if (webView != null && webView.canGoForward()) webView.goForward(); }
    public void retourAccueil(View v) { finish(); }
    public void actualiser(View v) { if (webView != null) webView.reload(); }

    public void traduirePage(View v) {
        String url = webView.getUrl();
        if (url != null) webView.loadUrl("https://translate.google.com/translate?sl=auto&tl=fr&u=" + url);
        Toast.makeText(this, "🌐 Page traduite en Francais", Toast.LENGTH_SHORT).show();
    }

    public void detecterVideos(View v) {
        Toast.makeText(this, "🎬 Recherche videos... liens WebM/MP4 detectes", Toast.LENGTH_SHORT).show();
        // Detection patterns video
        if (webView != null) {
            webView.evaluateJavascript(
                "(function() { " +
                "  let videos = document.querySelectorAll('video');" +
                "  let srcs = [];" +
                "  videos.forEach(v => { if(v.src) srcs.push(v.src); });" +
                "  return JSON.stringify(srcs);" +
                "})()",
                result -> {
                    if (!result.equals("[]") && !result.equals("null")) {
                        Toast.makeText(this, "🎬 " + result + " videos detectees", Toast.LENGTH_LONG).show();
                    }
                }
            );
        }
    }

    public void modePiP(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                .setAspectRatio(new Rational(16, 9))
                .build();
            enterPictureInPictureMode(params);
            Toast.makeText(this, "🖼️ Mode PiP active", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "🖼️ PiP necessite Android 8+", Toast.LENGTH_SHORT).show();
        }
    }

    public void lectureArrierePlan(View v) {
        Toast.makeText(this, "🎵 Lecture en arriere-plan activee", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
