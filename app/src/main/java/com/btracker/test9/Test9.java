package com.btracker.test9;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Test9 extends AppCompatActivity {

    /**
     * Instancia del drawer
     */
    private DrawerLayout drawerLayout;

    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;

    private Toolbar toolbar;

    /**
     * ImageView del buscador
     */
    private ImageView loadingView;

    /**
     * Animation del buscador
     */
    private AnimationDrawable loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test9);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            // Añadir carácteristicas
        }

        drawerTitle = getResources().getString(R.string.current_location_item);
        if (savedInstanceState == null) {
            // Seleccionar item
        }

        loadingView = (ImageView) findViewById(R.id.loadingView);

        // Obtener animación de loading
        if (loadingView != null) {
            loadingAnimation = (AnimationDrawable) loadingView.getBackground();
        }
        loadingAnimation.start();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono y logo del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setLogo(R.drawable.ic_action_descuentos);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Método lanzado al terminar Loading **/
    public void productDetail(View view) {
        loadingAnimation.stop();

        Intent detailIntent = new Intent(this, ProductActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        loadingAnimation.start();
    }

}
