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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.btracker.test9.async.EventsListener;
import com.btracker.test9.dto.Beacon;
import com.btracker.test9.json.JsonResponseDecoder;
import com.btracker.test9.web.DatabaseConnectivity;
import com.btracker.test9.web.VolleySingleton;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class Test9 extends AppCompatActivity implements EventsListener {

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

    /*
       Lista de Beacons
     */
    private Beacon[] beaconsList;

    /*
       Gestor de Beacons
     */
    private BeaconManager beaconManager;
    private Region region;

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

        // Obtener listado de Beacons
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        databaseConnectivity.getBeaconsList(this);

        // Instanciar gestor de Beacons
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<com.estimote.sdk.Beacon> list) {
                if (!list.isEmpty()) {
                    com.estimote.sdk.Beacon nearestBeacon = list.get(0);
                    Log.e("Beacon encontrado: ",nearestBeacon.getMacAddress().toString());
                    productDetail(nearestBeacon);
                }
            }
        });
        // TODO Definir región de escaneo según los resultados obtenidos en beaconsList
        // También podría definirse en onResume(), donde se setea
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
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
    // main_content.xml android:onClick="productDetail"
    public void productDetail(com.estimote.sdk.Beacon beacon) {
        loadingAnimation.stop();
        Intent detailIntent = new Intent(this, ProductActivity.class);
        detailIntent.putExtra("ProductBeacon",beacon);
        startActivity(detailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        loadingAnimation.start();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    @Override
    public void beaconsResult(JSONObject jsonResponse) {
        beaconsList = JsonResponseDecoder.beaconListResponse(jsonResponse);
        // Mensaje de prueba
        //Toast.makeText(this,beaconsList[0].getUuid(),Toast.LENGTH_LONG).show();
    }

}
