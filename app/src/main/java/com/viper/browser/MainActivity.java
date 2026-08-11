package com.viper.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    // Zone d'affichage des erreurs — TOUJOURS visible
    private TextView errorLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Charger le layout normal
            setContentView(R.layout.activity_main);
            
            // Afficher message de succès
            ajouterLog("✅ Application démarrée avec succès", Color.GREEN);

        } catch (Throwable t) {
            // --- ERREUR DÉTECTÉE — Afficher l'écran d'erreur ---
            t.printStackTrace();
            
            // Créer un écran d'erreur qui REMPLACE tout
            ScrollView scrollView = new ScrollView(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(Color.BLACK);
            layout.setPadding(32, 32, 32, 32);

            // Titre
            TextView titre = new TextView(this);
            titre.setText("❌ ERREUR DÉTECTÉE");
            titre.setTextColor(Color.RED);
            titre.setTextSize(24);
            titre.setPadding(0, 0, 0, 24);
            layout.addView(titre);

            // Message d'erreur
            errorLogView = new TextView(this);
            errorLogView.setTextColor(Color.YELLOW);
            errorLogView.setTextSize(14);
            errorLogView.setText(obtenirStackTrace(t));
            layout.addView(errorLogView);

            scrollView.addView(layout);
            setContentView(scrollView);
        }
    }

    // Ajouter un message dans le log
    private void ajouterLog(String message, int couleur) {
        // On peut aussi garder ça pour plus tard
    }

    // Convertir l'erreur complète en texte lisible
    private String obtenirStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append("MESSAGE: ").append(t.getMessage()).append("\n\n");
        sb.append("CAUSE: ").append(t.getCause()).append("\n\n");
        sb.append("--- PILE D'APPEL ---\n");
        for (StackTraceElement elt : t.getStackTrace()) {
            sb.append("  at ").append(elt.getClassName())
              .append(".").append(elt.getMethodName())
              .append("(").append(elt.getFileName())
              .append(":").append(elt.getLineNumber())
              .append(")\n");
        }
        return sb.toString();
    }
}
