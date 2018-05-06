package com.d.net_brains_test;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.d.net_brains_test.controller.FMenu;

/**
 * Created by Алексей on 05.05.2018.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Fragment fragment;
        if(savedInstanceState == null)
            fragment = new FMenu();
        else
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "current");
        getSupportFragmentManager().beginTransaction().replace(R.id.root_view, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "current", getSupportFragmentManager().findFragmentById(R.id.root_view));

    }
}
