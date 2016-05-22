package com.innovamos.btracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.dto.CustomerProductsDTO;
import com.innovamos.btracker.dto.PurchasesDTO;
import com.innovamos.btracker.fragments.PurchasedListFragment;
import com.innovamos.btracker.fragments.StartFragment;
import com.innovamos.btracker.fragments.WishListFragment;
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
    // Instancia del panel lateral
    private NavigationView navigationView;

    // Fragmentos
    private FragmentManager fragmentManager;

    // Cliente loggeado
    private CustomerDTO customerDTO;

    // Lista de productos deseados
    private CustomerProductsDTO[] wishedProductsList;
    // Lista de productos comprados
    private PurchasesDTO[] purchasedProductsList;

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

        // Instancia de Fragmentos
        fragmentManager = getSupportFragmentManager();
        // Crear fragmento en el contenedor principal (sobre el que se colocan todos los fragmentos
        fragmentManager.beginTransaction().add(R.id.main_container, new StartFragment()).commit();

        /*
        Configuracion de interfaz principal: Barra superior, panel lateral izquierdo
         */
        // Configurar Toolbar como action bar
        setToolbar();

        // Configurar acciones del panel lateral izquierdo
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        /*if (savedInstanceState == null) {
            // Seleccionar item
        }*/

        /* Configuración de las instancias de comunicación a base de datos:
           Lista de Beacons y Confirmar existencia de usuario */
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);

        // Obtener listado de Beacons
        databaseConnectivity.getBeaconsList(this);

        // Obtener MAC y Confirmar Existencia
        databaseConnectivity.getCustomer(this, getMacAddr());

        /* Configuración de búsqueda inicial de beacons */

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
            case R.id.main_notifications:
                showNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        if(region!=null){
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                }
            });
        }

        // Actualizar listas al retomar la actividad
        if(customerDTO!=null){
            DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
            // Obtener lista de productos con like
            databaseConnectivity.getProductsLike(this, customerDTO.getId());
            // Obtener lista de productos comprados
            databaseConnectivity.getPurchasedProducts(this, customerDTO.getId());
        }
    }

    @Override
    protected void onPause() {
        if(region!=null){
            beaconManager.stopRanging(region);
        }
        super.onPause();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        selectItem(title);
                        return true;
                    }
                }
        );
    }

    /**
     * Método que abre un fragment cuando el usuario presiona una acción del menú
     * @param title Titulo del fragmento
     */
    private void selectItem(String title) {
        Fragment selectedFragment = null;

        if (title.equals(getString(R.string.home_item))) {
            selectedFragment = StartFragment.newInstance();
            title = getString(R.string.app_name);
        }
        if (title.equals(getString(R.string.deseos_item))) {
            selectedFragment = WishListFragment.newInstance(wishedProductsList);
        }
        if (title.equals(getString(R.string.compras_item))) {
            selectedFragment = PurchasedListFragment.newInstance(purchasedProductsList);
        }
        if (title.equals(getString(R.string.visited_places_item))) {
            selectedFragment = new WishListFragment();
        }
        if (title.equals(getString(R.string.notification_item))) {
            selectedFragment = new WishListFragment();
        }
        if (title.equals(getString(R.string.log_out_item))) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        if (selectedFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.main_container, selectedFragment).commit();
            // Cerrar menu lateral
            drawerLayout.closeDrawers();
            // Setear título actual
            setTitle(title);
        }
    }

    private void showNotifications() {
        //TODO Create the proper fragment here
        Fragment notificationsFragment = new WishListFragment();

        fragmentManager.beginTransaction().replace(R.id.main_container, notificationsFragment).commit();
        // Cerrar menu lateral
        drawerLayout.closeDrawers();
        // Setear título actual
        setTitle(getString(R.string.notification_item));
        // Fijar item de notificaciones como seleccionado
        navigationView.setCheckedItem(R.id.nav_notificaciones);
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

    /**
     * Método lanzado al terminar Loading
     * @param beacon Beacon
     * @param customerDTO Cliente
     */
    public void productDetail(Beacon beacon, CustomerDTO customerDTO) {
        Intent detailIntent = new Intent(this, ProductActivity.class);
        detailIntent.putExtra("ProductBeacon", beacon);
        detailIntent.putExtra("Customer", customerDTO.getId());
        startActivity(detailIntent);
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
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            return "02:00:00:00:00:00";
        }

        return null;
    }

    /**
     * Método que obtiene la respuesta de base de datos con la lista de beacons
     * @param jsonResponse Objeto JSON a decodificar
     */
    @Override
    public void beaconsListResult(JSONObject jsonResponse) {
        BeaconDTO[] beaconsList = JsonResponseDecoder.beaconListResponse(jsonResponse);
        if (beaconsList != null) {
            region = new Region("Ranged Beacons Region", UUID.fromString(beaconsList[0].getUuid()), null, null);
        }
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        // Mensaje de prueba
        //Toast.makeText(this,beaconsList[0].getUuid(),Toast.LENGTH_LONG).show();
    }

    /**
     * Método que obtiene la respuesta de base de datos con el usuario loggeado
     * @param jsonResponse Objeto JSON a decodificar
     */
    @Override
    public void customerResult(JSONObject jsonResponse) {
        customerDTO = JsonResponseDecoder.customerResponse(jsonResponse);
        if(customerDTO!=null){
            DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
            // Obtener lista de productos con like
            databaseConnectivity.getProductsLike(this,customerDTO.getId());
            // Obtener lista de productos comprados
            databaseConnectivity.getPurchasedProducts(this, customerDTO.getId());
        }
        else{
            Toast.makeText(this,"Couldn´t find any user", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void zoneResult(JSONObject jsonResult) {

    }

    @Override
    public void productsZoneList(JSONObject jsonResult) {

    }

    @Override
    public void productsLikeList(JSONObject jsonResult) {
        wishedProductsList = JsonResponseDecoder.productsLikeListResponse(jsonResult);
    }

    @Override
    public void insertProductLike(JSONObject jsonResult) {

    }

    @Override
    public void deleteProductLike(JSONObject jsonResult) {

    }

    @Override
    public void purchasedProductsList(JSONObject jsonResult) {
        purchasedProductsList = JsonResponseDecoder.purchasedProductsResponse(jsonResult);
    }

    @Override
    public void insertProductPurchase(JSONObject jsonResult) {

    }

    @Override
    public void deleteProductPurchase(JSONObject jsonResult) {

    }
}
