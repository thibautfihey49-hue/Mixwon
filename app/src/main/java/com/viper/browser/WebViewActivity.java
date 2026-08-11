package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private TextView urlAffichage;
    private SharedPreferences prefs;
    private boolean adBlockEnabled;

    // Liste des domaines publicitaires à bloquer
    private static final Set<String> AD_DOMAINS = new HashSet<>(Arrays.asList(
        "ad.", "ads.", "adserver.", "advertising.", "banner.", "tracking.",
        "analytics.", "track.", "stats.", "pixel.", "beacon.", "adnetwork.",
        "doubleclick.net", "googlesyndication.com", "facebook.com/tr", "amazon-adsystem.com",
        "adroll.com", "criteo.com", "taboola.com", "outbrain.com", "hotjar.com"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
        adBlockEnabled = prefs.getBoolean("adblock_enabled", false);

        try {
            webView = findViewById(R.id.webView);
            urlAffichage = findViewById(R.id.urlAffichage);
            configurerWebView();

            String url = getIntent().getStringExtra("url");
            if (url != null) webView.loadUrl(url);
            else webView.loadUrl("https://google.com");
        } catch (Exception e) {
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurerWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                urlAffichage.setText(url.replace("https://", "").replace("http://", ""));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (adBlockEnabled && estUrlPub(url)) {
                    Toast.makeText(WebViewActivity.this, "🛡️ Publicité bloquée", Toast.LENGTH_SHORT).show();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    private boolean estUrlPub(String url) {
        for (String domaine : AD_DOMAINS) {
            if (url.contains(domaine)) return true;
        }
        return false;
    }

    public void precedent(View v) { if (webView != null && webView.canGoBack()) webView.goBack(); }
    public void suivant(View v) { if (webView != null && webView.canGoForward()) webView.goForward(); }
    public void retourAccueil(View v) { finish(); }
    public void actualiser(View v) { if (webView != null) webView.reload(); }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
