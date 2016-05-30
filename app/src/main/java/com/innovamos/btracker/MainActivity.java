package com.innovamos.btracker;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
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

import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.async.FragmentCommunicator;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.dto.CustomerProductsDTO;
import com.innovamos.btracker.dto.PurchasesDTO;
import com.innovamos.btracker.dto.VisitsDTO;
import com.innovamos.btracker.fragments.MessageFragment;
import com.innovamos.btracker.fragments.NotificationsListFragment;
import com.innovamos.btracker.fragments.PurchasedListFragment;
import com.innovamos.btracker.fragments.StartFragment;
import com.innovamos.btracker.fragments.VisitsListFragment;
import com.innovamos.btracker.fragments.WishListFragment;
import com.innovamos.btracker.json.JsonResponseDecoder;
import com.innovamos.btracker.sync.BtrackerSyncAdapter;
import com.innovamos.btracker.web.DatabaseConnectivity;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements EventListener {

    /**
     * Interfaz principal
     */
    // Instancia del drawer
    private DrawerLayout drawerLayout;
    // Instancia del panel lateral
    private NavigationView navigationView;

    // Interfaz de comunicación con el fragmento principal
    public FragmentCommunicator fc ;

    private Fragment mainFragment;
    private Fragment messageFragment;
    private Fragment startFragment;

    // Fragmentos
    private FragmentManager fragmentManager;

    // Cliente loggeado
    private CustomerDTO customerDTO;

    // Lista de productos deseados
    private CustomerProductsDTO[] wishedProductsList;
    // Lista de productos comprados
    private PurchasesDTO[] purchasedProductsList;
    // Lista de visitas realizadas por el usuario
    private VisitsDTO[] customerVisitsList;
    // Lista de notificaciones recibidas por el usuario
    private VisitsDTO[] customerNotificationsList;

    /*
     * Gestor de Beacons
     */
    /*
    // Gestor de Beacons
    private BeaconManager beaconManager;
    // Regiones de escaneo
    private Region region;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Instancia de Fragmentos
        fragmentManager = getSupportFragmentManager();

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

        startFragment = StartFragment.newInstance();
        messageFragment = MessageFragment.newInstance(getString(R.string.info), getString(R.string.bluetooth_info));
        mainFragment = isBluetoothAvailable() ? startFragment : messageFragment;
        fragmentManager.beginTransaction().replace(R.id.main_container, mainFragment).commit();

        /*if (savedInstanceState == null) {
            // Seleccionar item
        }*/

        BtrackerSyncAdapter.initializeSyncAdapter(this);

        /* Configuración de las instancias de comunicación a base de datos:
           Lista de Beacons y Confirmar existencia de usuario */
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        databaseConnectivity.getBeaconsList(this);
        databaseConnectivity.getCustomer(this);

        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);


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
            case R.id.main_menu_help:
                showHelp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        // Actualizar listas al retomar la actividad
        if(customerDTO!=null){
            DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
            // Obtener lista de productos con like
            databaseConnectivity.getProductsLike(this, customerDTO.getId());
            // Obtener lista de productos comprados
            databaseConnectivity.getPurchasedProducts(this, customerDTO.getId());
            // Obtener lista de visitas realizadas por el cliente
            databaseConnectivity.getCustomerVisits(this, customerDTO.getId());
            // Obtener lista de notificationes no vistas por el cliente
            databaseConnectivity.getCustomerNotifications(this, customerDTO.getId());
        }
    }

    @Override
    protected void onPause() {
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
            selectedFragment = mainFragment;
            title = getString(R.string.app_name);
        }
        if (title.equals(getString(R.string.deseos_item))) {
            selectedFragment = WishListFragment.newInstance(wishedProductsList);
        }
        if (title.equals(getString(R.string.compras_item))) {
            selectedFragment = PurchasedListFragment.newInstance(purchasedProductsList);
        }
        if (title.equals(getString(R.string.visited_places_item))) {
            selectedFragment = VisitsListFragment.newInstance(customerVisitsList);
        }
        if (title.equals(getString(R.string.notification_item))) {
            selectedFragment = NotificationsListFragment.newInstance(customerNotificationsList);
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
        // Cargar el fragmento de notificaciones
        fragmentManager.beginTransaction().replace(R.id.main_container, NotificationsListFragment.newInstance(customerNotificationsList)).commit();
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
     * Check for Bluetooth.
     * @return True if Bluetooth is available.
     */
    public static boolean isBluetoothAvailable() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Bluetooth: ", "Status changed");

            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mainFragment = messageFragment;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mainFragment = startFragment;
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.main_container, mainFragment).commit();
            }
        }
    };

    /**
     * Método que obtiene la respuesta de base de datos con la lista de beacons
     * @param jsonResponse Objeto JSON a decodificar
     */
    @Override
    public void beaconsListResult(JSONObject jsonResponse) {
        BeaconDTO[] beaconsList = JsonResponseDecoder.beaconListResponse(jsonResponse);
        if (beaconsList != null) {
            // Pasar la región por interfaz al fragmento start
            fc.setBeaconList(beaconsList);
        }
    }

    /**
     * Método que obtiene la respuesta de base de datos con el usuario loggeado
     * @param jsonResponse Objeto JSON a decodificar
     */
    @Override
    public void customerResult(JSONObject jsonResponse) {
        customerDTO = JsonResponseDecoder.customerResponse(jsonResponse);
        if(customerDTO!=null) {
            fc.setCustomer(customerDTO);

            DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
            // Obtener lista de productos con like
            databaseConnectivity.getProductsLike(this,customerDTO.getId());
            // Obtener lista de productos comprados
            databaseConnectivity.getPurchasedProducts(this, customerDTO.getId());
            // Obtener lista de visitas realizadas por el cliente
            databaseConnectivity.getCustomerVisits(this, customerDTO.getId());
            // Obtener lista de notificationes no vistas por el cliente
            databaseConnectivity.getCustomerNotifications(this, customerDTO.getId());
        }
        else{
            Toast.makeText(this,"Couldn´t find any user", Toast.LENGTH_LONG).show();
        }
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    /** Called when the user clicks the Help item
     *
     */
    public void showHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
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

    @Override
    public void customerVisitsList(JSONObject jsonResult) {
        customerVisitsList = JsonResponseDecoder.visitsListResponse(jsonResult);
    }

    @Override
    public void customerNotificationsList(JSONObject jsonResult) {
        customerNotificationsList = JsonResponseDecoder.notificationsListResponse(jsonResult);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }
}
