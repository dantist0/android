package com.d.test_for_job;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.d.test_for_job.controller.FProductList;

/**
 * Created by Алексей on 08.04.2018.
 */

public class MainActivity extends AppCompatActivity {
    public Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FProductList()).commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if(backStackCount >0){
                getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
