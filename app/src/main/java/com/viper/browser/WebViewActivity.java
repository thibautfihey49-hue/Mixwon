package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url != null ? url : "https://google.com");
    }

    public void precedent(View v) { if (webView != null) webView.goBack(); }
    public void suivant(View v) { if (webView != null) webView.goForward(); }
    public void retourAccueil(View v) { finish(); }
    public void actualiser(View v) { if (webView != null) webView.reload(); }
    public void voirHistorique(View v) { Toast.makeText(this, "Historique", Toast.LENGTH_SHORT).show(); }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
