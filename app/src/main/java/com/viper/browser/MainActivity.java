package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.searchBar);
    }

    public void openGoogle(View v) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "https://google.com"));
    }
    public void openYouTube(View v) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "https://youtube.com"));
    }
    public void openInstagram(View v) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "https://instagram.com"));
    }
    public void openReddit(View v) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "https://reddit.com"));
    }
    public void toggleAdBlock(View v) { Toast.makeText(this, "AdBlock", Toast.LENGTH_SHORT).show(); }
    public void openIncognito(View v) { Toast.makeText(this, "Incognito", Toast.LENGTH_SHORT).show(); }
    public void toggleNightMode(View v) { Toast.makeText(this, "Mode Nuit", Toast.LENGTH_SHORT).show(); }
    public void toggleDataSaving(View v) { Toast.makeText(this, "Éco données", Toast.LENGTH_SHORT).show(); }
    public void openFavorites(View v) { Toast.makeText(this, "Favoris", Toast.LENGTH_SHORT).show(); }
    public void toggleAutoClear(View v) { Toast.makeText(this, "Auto-Clear", Toast.LENGTH_SHORT).show(); }
    public void effacerTout(View v) { searchBar.setText(""); }
    public void openSettings(View v) { Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show(); }
}
