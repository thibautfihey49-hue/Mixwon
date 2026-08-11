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
                chercherOuOuvrir();
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void chercherOuOuvrir() {
        String requete = searchBar.getText().toString().trim();
        if (requete.isEmpty()) {
            Toast.makeText(this, "Saisis une URL", Toast.LENGTH_SHORT).show();
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

    public void openIncognito(View v) { Toast.makeText(this, "Mode Incognito bientôt", Toast.LENGTH_SHORT).show(); }
    public void toggleAdBlock(View v) { Toast.makeText(this, "AdBlock bientôt", Toast.LENGTH_SHORT).show(); }
    public void toggleNightMode(View v) { Toast.makeText(this, "Mode Nuit bientôt", Toast.LENGTH_SHORT).show(); }
    public void toggleDataSaving(View v) { Toast.makeText(this, "Éco données bientôt", Toast.LENGTH_SHORT).show(); }
    public void toggleAutoClear(View v) { Toast.makeText(this, "Auto-Clear bientôt", Toast.LENGTH_SHORT).show(); }
    public void openFavorites(View v) { Toast.makeText(this, "Favoris bientôt", Toast.LENGTH_SHORT).show(); }
    public void effacerTout(View v) { searchBar.setText(""); }
    public void openSettings(View v) { Toast.makeText(this, "Paramètres bientôt", Toast.LENGTH_SHORT).show(); }
}
