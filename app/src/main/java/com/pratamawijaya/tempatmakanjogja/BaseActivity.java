package com.pratamawijaya.tempatmakanjogja;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by pratama on 12/24/14.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    /**
     * get toolbar
     *
     * @return
     */
    protected Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }
}
