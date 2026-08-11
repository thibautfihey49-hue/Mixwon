package com.viper.browser;

import android.app.Application;
import android.content.SharedPreferences;

public class ViperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // ===== DETECTER SI L'APP S'EST CRASHÉE =====
        SharedPreferences prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
        boolean etatExecution = prefs.getBoolean("app_en_cours", false);
        
        // Si app_en_cours = true au demarrage, c'est que l'app s'est fermee anormalement
        if (etatExecution) {
            prefs.edit().putBoolean("app_crashee", true).apply();
        }
        
        // Marquer l'app comme en cours
        prefs.edit().putBoolean("app_en_cours", true).apply();
        
        // ===== INTERCEPTEUR DE CRASH EN TEMPS RÉEL =====
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // Enregistrer que l'app a crashé
                SharedPreferences prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
                prefs.edit()
                    .putBoolean("app_en_cours", false)
                    .putBoolean("app_crashee", true)
                    .putString("derreur", e.getMessage())
                    .apply();
                
                // Laisser le systeme afficher le message system
                System.exit(1);
            }
        });
    }
}
