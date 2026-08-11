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
        try {
            setContentView(R.layout.activity_main);
            searchBar = findViewById(R.id.searchBar);
            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                validerRecherche(v);
                return true;
            });
        } catch (Throwable t) {
            Toast.makeText(this, "ERREUR: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void validerRecherche(View v) {
        String requete = searchBar.getText().toString().trim();
        if (requete.isEmpty()) {
            Toast.makeText(this, "Saisis une URL ou un mot-clé 🔍", Toast.LENGTH_SHORT).show();
            return;
        }
        String url;
        if (requete.startsWith("http://") || requete.startsWith("https://")) {
            url = requete;
        } else if (requete.contains(".") && !requete.contains(" ")) {
            url = "https://" + requete;
        } else {
            url = "https://www.google.com/search?q=" + requete.replace(" ", "+");
        }
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", url));
        searchBar.setText("");
    }

    public void openGoogle(View v) { ouvrir("https://google.com"); }
    public void openYouTube(View v) { ouvrir("https://youtube.com"); }
    public void openInstagram(View v) { ouvrir("https://instagram.com"); }
    public void openReddit(View v) { ouvrir("https://reddit.com"); }

    private void ouvrir(String url) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", url));
    }

    public void toggleAdBlock(View v) { Toast.makeText(this, "🛡️ AdBlock activé", Toast.LENGTH_SHORT).show(); }
    public void openIncognito(View v) { Toast.makeText(this, "🕵️ Mode Incognito", Toast.LENGTH_SHORT).show(); }
    public void toggleNightMode(View v) { Toast.makeText(this, "🌙 Mode Nuit", Toast.LENGTH_SHORT).show(); }
    public void toggleDataSaving(View v) { Toast.makeText(this, "⚡ Éco données", Toast.LENGTH_SHORT).show(); }
    public void openFavorites(View v) { Toast.makeText(this, "⭐ Favoris", Toast.LENGTH_SHORT).show(); }
    public void toggleAutoClear(View v) { Toast.makeText(this, "🧹 Auto-Clear", Toast.LENGTH_SHORT).show(); }
    public void effacerTout(View v) { searchBar.setText(""); Toast.makeText(this, "🗑️ Barre effacée", Toast.LENGTH_SHORT).show(); }
    public void openSettings(View v) { Toast.makeText(this, "⚙️ Paramètres", Toast.LENGTH_SHORT).show(); }
}
