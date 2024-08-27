package com.java.uidemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.adapter.DemoScreenAdapter;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.model.DemoScreen;

/**
 * This {@link AppCompatActivity} displays a {@link RecyclerView} grid. Each element launches a different {@link com.java.uidemo.util.DemoActivity}.
 */
public class MenuActivity extends AppCompatActivity
{
    private UIDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        demo = (UIDemo) getApplicationContext();
        RecyclerView rv_demo_screen = findViewById(R.id.rv_demo_screen_menu);

        /*
         * An attribute that is true in <b>/res/values-sw600dp</b> and false otherwise.
         */
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize)
        {
            rv_demo_screen.setLayoutManager(new GridLayoutManager(MenuActivity.this,6));
        }
        else
        {
            rv_demo_screen.setLayoutManager(new GridLayoutManager(MenuActivity.this,3));
        }
        DemoScreenAdapter demo_screen_adapter = new DemoScreenAdapter(demo.getDemo_screens(), MenuActivity.this);
        rv_demo_screen.setAdapter(demo_screen_adapter);
    }

    public void launchDemoScreen(DemoScreen demo_screen)
    {
        Intent i = new Intent(MenuActivity.this, demo_screen.getClass_reference());
        demo.setDemo_screen(demo_screen);
        startActivity(i);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        finish();
    }
}
