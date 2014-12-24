package com.pratamawijaya.tempatmakanjogja;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends ActionBarActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMapIfNeeded();
    }

    /**
     * initialize Google Map object
     */
    private void setupMapIfNeeded() {
        if (map == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            map = supportMapFragment.getMap();

            if (map != null) {
                setupMap();
            }
        }
    }

    /**
     * map is ready to rock
     */
    private void setupMap() {

    }

}
