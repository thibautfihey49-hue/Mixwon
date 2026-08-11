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
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            validerRecherche(v);
            return true;
        });
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
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        searchBar.setText("");
    }

    public void openGoogle(View v) { ouvrir("https://google.com"); }
    public void openYouTube(View v) { ouvrir("https://youtube.com"); }
    public void openInstagram(View v) { ouvrir("https://instagram.com"); }
    public void openReddit(View v) { ouvrir("https://reddit.com"); }

    private void ouvrir(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    // ========== MÉTHODES DE LA BARRE DE NAVIGATION ==========
    public void retourAccueil(View v) {
        Toast.makeText(this, "🏠 Accueil", Toast.LENGTH_SHORT).show();
        // Revient déjà ici, on est sur l'accueil
    }

    public void pagePrecedente(View v) {
        Toast.makeText(this, "◀ Précédent — disponible dans la page web", Toast.LENGTH_SHORT).show();
    }

    public void pageSuivante(View v) {
        Toast.makeText(this, "▶ Suivant — disponible dans la page web", Toast.LENGTH_SHORT).show();
    }

    public void actualiser(View v) {
        Toast.makeText(this, "🔄 Actualiser", Toast.LENGTH_SHORT).show();
    }

    public void ouvrirFavoris(View v) {
        Toast.makeText(this, "⭐ Favoris — bientôt disponible", Toast.LENGTH_SHORT).show();
    }
}
