package com.java.uidemo.demos;

import static com.java.uidemo.util.Constants.LANGUAGE_PREFERENCE;
import static com.java.uidemo.util.Constants.SHARED_PREFERENCES;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.R;
import com.java.uidemo.adapter.LanguageAdapter;
import com.java.uidemo.model.DemoLanguage;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

/**
 * This {@link DemoActivity} displays a {@link RecyclerView} grid. Clicking on the different elements changes the language of the app.
 */
public class LanguageActivity extends DemoActivity
{
    private LanguageAdapter language_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initDemo();
        RecyclerView rv_languages = findViewById(R.id.rv_languages_language);
        language_adapter = new LanguageAdapter(demo.getLanguages(),LanguageActivity.this);
        rv_languages.setLayoutManager(new GridLayoutManager(LanguageActivity.this,3));
        rv_languages.setAdapter(language_adapter);
    }
    public void selectLanguage(DemoLanguage language)
    {
        Util.setLocale(LanguageActivity.this,language.getIso_code());
        getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE).edit().putString(LANGUAGE_PREFERENCE,language.getIso_code()).apply();
        initDemo();
        language_adapter.notifyDataSetChanged();
    }
}
