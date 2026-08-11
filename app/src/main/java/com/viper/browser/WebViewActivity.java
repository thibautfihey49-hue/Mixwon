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
        
        try {
            webView = findViewById(R.id.webView);
            
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAllowFileAccess(false);
            settings.setAllowContentAccess(false);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            
            String url = getIntent().getStringExtra("url");
            if (url != null) {
                webView.loadUrl(url);
            } else {
                webView.loadUrl("https://google.com");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur WebView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void precedent(View v) { if (webView != null && webView.canGoBack()) webView.goBack(); }
    public void suivant(View v) { if (webView != null && webView.canGoForward()) webView.goForward(); }
    public void retourAccueil(View v) { finish(); }
    public void actualiser(View v) { if (webView != null) webView.reload(); }
    public void voirHistorique(View v) { Toast.makeText(this, "Historique bientôt", Toast.LENGTH_SHORT).show(); }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
