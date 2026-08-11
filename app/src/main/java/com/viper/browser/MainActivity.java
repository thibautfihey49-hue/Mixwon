package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "ViperBrowserPrefs";
    private static final String KEY_ADBLOCK = "adblock_enabled";
    private static final String KEY_INCOGNITO = "incognito_mode";
    public static final String KEY_HISTORY = "history_list";
    public static final String KEY_FAVORITES = "favorites_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
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
        ajouterHistorique(url);
        ouvrirUrl(url);
        searchBar.setText("");
    }

    public void openGoogle(View v) { ouvrir("https://google.com"); }
    public void openYouTube(View v) { ouvrir("https://youtube.com"); }
    public void openInstagram(View v) { ouvrir("https://instagram.com"); }
    public void openReddit(View v) { ouvrir("https://reddit.com"); }

    private void ouvrir(String url) {
        ajouterHistorique(url);
        ouvrirUrl(url);
    }

    private void ouvrirUrl(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    // ========== BARRE DE NAVIGATION ==========
    public void retourAccueil(View v) {
        Toast.makeText(this, "🏠 Accueil", Toast.LENGTH_SHORT).show();
    }

    public void pagePrecedente(View v) {
        Toast.makeText(this, "◀ Précédent — dans la page web", Toast.LENGTH_SHORT).show();
    }

    public void pageSuivante(View v) {
        Toast.makeText(this, "▶ Suivant — dans la page web", Toast.LENGTH_SHORT).show();
    }

    public void actualiser(View v) {
        Toast.makeText(this, "🔄 Actualiser", Toast.LENGTH_SHORT).show();
    }

    public void openSettings(View v) {
        Toast.makeText(this, "⚙️ Paramètres bientôt disponible", Toast.LENGTH_SHORT).show();
    }

    // ========== FONCTIONS OUTILS ==========
    public void toggleAdBlock(View v) {
        boolean estActif = prefs.getBoolean(KEY_ADBLOCK, false);
        prefs.edit().putBoolean(KEY_ADBLOCK, !estActif).apply();
        Toast.makeText(this, estActif ? "🛡️ AdBlock DÉSACTIVÉ" : "🛡️ AdBlock ACTIVÉ", Toast.LENGTH_SHORT).show();
    }

    public void openIncognito(View v) {
        boolean estActif = prefs.getBoolean(KEY_INCOGNITO, false);
        prefs.edit().putBoolean(KEY_INCOGNITO, !estActif).apply();
        Toast.makeText(this, estActif ? "🕵️ Mode privé DÉSACTIVÉ" : "🕵️ Mode privé ACTIVÉ", Toast.LENGTH_SHORT).show();
    }

    public void openFavorites(View v) {
        Toast.makeText(this, "⭐ Favoris — bientôt disponible", Toast.LENGTH_SHORT).show();
    }

    public void openHistory(View v) {
        Toast.makeText(this, "📜 Historique — " + compterHistorique() + " pages", Toast.LENGTH_SHORT).show();
    }

    // ========== GESTION HISTORIQUE ==========
    private void ajouterHistorique(String url) {
        if (prefs.getBoolean(KEY_INCOGNITO, false)) return;
        String historique = prefs.getString(KEY_HISTORY, "");
        if (!historique.isEmpty()) historique += "|";
        prefs.edit().putString(KEY_HISTORY, historique + url).apply();
    }

    private int compterHistorique() {
        String historique = prefs.getString(KEY_HISTORY, "");
        if (historique.isEmpty()) return 0;
        return historique.split("\\|").length;
    }
}
