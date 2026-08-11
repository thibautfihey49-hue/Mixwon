package com.viper.browser;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class ViperApplication extends Application {
    private static final String TAG = "ViperBrowser";

    @Override
    public void onCreate() {
        super.onCreate();
        
        try {
            SharedPreferences prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
            boolean etatExecution = prefs.getBoolean("app_en_cours", false);
            
            // Marquer le crash seulement si l'app était marquee "en cours"
            if (etatExecution) {
                prefs.edit().putBoolean("app_crashee", true).apply();
                Log.w(TAG, "⚠️ Detection: fermeture anormale lors de la derniere session");
            }
            
            // Marquer l'app comme en cours MAINTENANT
            prefs.edit().putBoolean("app_en_cours", true).apply();
            
            // Intercepter les crashes
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    try {
                        SharedPreferences prefs = getSharedPreferences("ViperBrowserPrefs", MODE_PRIVATE);
                        prefs.edit()
                            .putBoolean("app_en_cours", false)
                            .putBoolean("app_crashee", true)
                            .putString("derreur", e.getMessage() != null ? e.getMessage() : "erreur inconnue")
                            .apply();
                    } catch (Exception ex) {
                        Log.e(TAG, "Impossible d'enregistrer le crash", ex);
                    }
                    
                    // Laisser le systeme gerer
                    System.exit(1);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erreur dans Application.onCreate", e);
        }
    }
}
