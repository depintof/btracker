package com.innovamos.btracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.estimote.sdk.Beacon;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.json.JsonResponseDecoder;
import com.innovamos.btracker.web.DatabaseConnectivity;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements EventListener {

    /**
     * Interfaz principal
     */
    // Instancia del drawer
    private DrawerLayout drawerLayout;
    // Titulo inicial del drawer
    private String drawerTitle;
    // Barra principal
    private Toolbar toolbar;
    // Imagen de carga
    private ImageView loadingView;
    // Animation del Buscador
    private AnimationDrawable loadingAnimation;

    /*
     * Base de Datos: Objetos
     */
    // Lista de Beacons
    private BeaconDTO[] beaconsList;
    // Cliente loggeado
    private CustomerDTO customerDTO;

    /*
     * Gestor de Beacons
     */
    // Gestor de Beacons
    private BeaconManager beaconManager;
    // Regiones de escaneo
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /*
        Configuracion de interfaz principal: Barra superior, panel lateral izquierdo
         */
        // Setear Toolbar como action bar
        setToolbar();
        // Setear acciones del panel lateral izquierdo
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            // Añadir caacterísticas
        }
        drawerTitle = getResources().getString(R.string.current_location_item);
        if (savedInstanceState == null) {
            // Seleccionar item
        }

        /*
        Configuración de la imagen de carga
         */
        loadingView = (ImageView) findViewById(R.id.loadingView);
        // Obtener animación de loading
        if (loadingView != null) {
            loadingAnimation = (AnimationDrawable) loadingView.getBackground();
        }
        loadingAnimation.start();

        /*
        Configuración de las instancias de comunicación a base de datos: Lista de Beacons y Confirmar existencia de usuario
         */
        // Obtener listado de Beacons
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        databaseConnectivity.getBeaconsList(this);
        // Obtener MAC y Confirmar Existencia
        databaseConnectivity.getCustomer(this,getMacAddr());

        /*
        Configuración de búsqueda inicial de beacons
         */
        // Instanciar gestor de Beacons
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<com.estimote.sdk.Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    Log.e("Beacon encontrado: ",nearestBeacon.getMacAddress().toString());
                    productDetail(nearestBeacon,customerDTO);
                }
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        loadingAnimation.start();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        if(region!=null){
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        if(region!=null){
            beaconManager.stopRanging(region);
        }
        super.onPause();
    }

    // Método para configurar la toolbar
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

    /** Método lanzado al terminar Loading **/
    // main_content.xml android:onClick="productDetail"
    public void productDetail(Beacon beacon, CustomerDTO customerDTO) {
        loadingAnimation.stop();
        Intent detailIntent = new Intent(this, ProductActivity.class);
        detailIntent.putExtra("ProductBeacon",beacon);
        detailIntent.putExtra("Customer", customerDTO.getId());
        startActivity(detailIntent);
    }

    // Método que obtiene la respuesta de base de datos con la lista de beacons
    @Override
    public void beaconsListResult(JSONObject jsonResponse) {
        beaconsList = JsonResponseDecoder.beaconListResponse(jsonResponse);
        region = new Region("Ranged Beacons Region", UUID.fromString(beaconsList[0].getUuid()), null, null);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        // Mensaje de prueba
        //Toast.makeText(this,beaconsList[0].getUuid(),Toast.LENGTH_LONG).show();
    }

    // Método que obtiene la respuesta de base de datos con el usuario loggeado
    @Override
    public void customerResult(JSONObject jsonResponse) {
        customerDTO = JsonResponseDecoder.customerResponse(jsonResponse);
        //if (customerDTO != null) {
        //    Toast.makeText(this,customerDTO.getMac(),Toast.LENGTH_LONG).show();
        //}
    }

    @Override
    public void zoneResult(JSONObject jsonResult) {

    }

    @Override
    public void productsZoneList(JSONObject jsonResult) {

    }

    @Override
    public void productsLikeList(JSONObject jsonResult) {

    }

    @Override
    public void insertProductLike(JSONObject jsonResult) {

    }

    @Override
    public void deleteProductLike(JSONObject jsonResult) {

    }

    // Método que obtiene la MAC del dispositivo movil
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

}
