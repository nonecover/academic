package com.jakomulski.datacollector.helpers;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public enum Language {
    POLISH("PL"),
    ENGLISH("EN");

    private String language;

    Language(String local) {
        this.language = local;
    }
    public void set(Context baseContext) {
        String languageToLoad = language;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config,
                baseContext.getResources().getDisplayMetrics());
    }
}

