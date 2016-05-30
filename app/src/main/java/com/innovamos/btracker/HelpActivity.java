package com.innovamos.btracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.innovamos.btracker.utils.PagerAdapter;

public class HelpActivity extends AppCompatActivity {

    ViewPager mViewPager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono y logo del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.title_activity_help);
        }

        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(pAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // If App icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}