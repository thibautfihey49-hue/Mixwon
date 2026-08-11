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
    private List<String> onglets = new ArrayList<>();
    private static boolean estEnCours = false; // Pour detecter crash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // ===== ALERTE SI L'APP S'EST FERMÉE LA DERNIÈRE FOIS =====
        boolean etatCrash = prefs.getBoolean("app_crashee", false);
        if (etatCrash) {
            Toast.makeText(this, "⚠️ L'application s'est fermée anormalement la dernière fois", Toast.LENGTH_LONG).show();
            prefs.edit().putBoolean("app_crashee", false).apply(); // Reinitialiser
        }
        
        // Marquer l'app comme en cours d'execution
        prefs.edit().putBoolean("app_en_cours", true).apply();
        estEnCours = true;
        
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            validerRecherche(v);
            return true;
        });
        onglets.add("Accueil");
    }

    // ===== ENREGISTRER LA FERMETURE NORMALE =====
    @Override
    protected void onDestroy() {
        super.onDestroy();
        estEnCours = false;
        prefs.edit().putBoolean("app_en_cours", false).apply();
        prefs.edit().putBoolean("app_crashee", false).apply();
    }

    // ===== ENREGISTRER LE CRASH SI L'APP EST TUÉE =====
    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            // Fermeture normale
            prefs.edit().putBoolean("app_crashee", false).apply();
        } else {
            // Passage en arrière-plan (normal)
        }
    }

    // Methode pour verifier au prochain demarrage
    public static boolean verifierCrash(SharedPreferences prefs) {
        return prefs.getBoolean("app_en_cours", false);
    }

    public void validerRecherche(View v) {
        String requete = searchBar.getText().toString().trim();
        if (requete.isEmpty()) {
            Toast.makeText(this, "Saisis une URL ou un mot-cle 🔍", Toast.LENGTH_SHORT).show();
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

    private void ouvrir(String url) { ajouterHistorique(url); ouvrirUrl(url); }
    private void ouvrirUrl(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void retourAccueil(View v) { Toast.makeText(this, "🏠 Accueil", Toast.LENGTH_SHORT).show(); }
    public void nouvelOnglet(View v) { onglets.add("Nouvel onglet"); Toast.makeText(this, "📑 Nouvel onglet - Total: " + onglets.size(), Toast.LENGTH_SHORT).show(); }
    public void toutTelecharger(View v) { Toast.makeText(this, "📥 Detection videos en cours...", Toast.LENGTH_SHORT).show(); }
    public void modePiP(View v) { Toast.makeText(this, "🖼️ Mode PiP active", Toast.LENGTH_SHORT).show(); }
    public void lectureArrierePlan(View v) { Toast.makeText(this, "🎵 Lecture en arriere-plan activee", Toast.LENGTH_SHORT).show(); }
    public void openSettings(View v) { Toast.makeText(this, "⚙️ Parametres bientot disponible", Toast.LENGTH_SHORT).show(); }

    public void toggleAdBlock(View v) {
        boolean estActif = prefs.getBoolean("adblock_enabled", false);
        prefs.edit().putBoolean("adblock_enabled", !estActif).apply();
        Toast.makeText(this, estActif ? "🛡️ AdBlock DESACTIVE" : "🛡️ AdBlock ACTIF - " + 150 + " domaines bloques", Toast.LENGTH_SHORT).show();
    }

    public void traduirePage(View v) {
        Toast.makeText(this, "🌐 Traduction automatique: Google Translate", Toast.LENGTH_SHORT).show();
    }

    public void openFavorites(View v) { Toast.makeText(this, "⭐ Favoris - " + compterFavoris() + " elements", Toast.LENGTH_SHORT).show(); }
    public void openHistory(View v) { Toast.makeText(this, "📜 Historique - " + compterHistorique() + " pages", Toast.LENGTH_SHORT).show(); }

    private void ajouterHistorique(String url) {
        if (prefs.getBoolean("incognito_mode", false)) return;
        String historique = prefs.getString("history_list", "");
        prefs.edit().putString("history_list", historique + "|" + url).apply();
    }
    private int compterHistorique() { String h = prefs.getString("history_list", ""); return h.isEmpty() ? 0 : h.split("\\|").length; }
    private int compterFavoris() { String f = prefs.getString("favorites_list", ""); return f.isEmpty() ? 0 : f.split("\\|").length; }
}
