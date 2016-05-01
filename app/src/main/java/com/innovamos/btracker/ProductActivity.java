package com.innovamos.btracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.dto.ProductDTO;
import com.innovamos.btracker.dto.ZoneDTO;
import com.innovamos.btracker.json.JsonMessageEncoder;
import com.innovamos.btracker.json.JsonResponseDecoder;
import com.innovamos.btracker.web.DatabaseConnectivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ProductActivity extends AppCompatActivity implements EventListener{

    //private Integer images[] = {R.drawable.gafas, R.drawable.gorra, R.drawable.pantalon,R.drawable.pic4};

    /*
    Variables de Interfaz Gráfica
     */
    Toolbar toolbar;
    TextView tvProducto;
    TextView tvDescripcion;
    TextView tvPrecioConDescuento;
    TextView tvPrecioOriginal;
    TextView tvDescuento;
    ImageView displayImage;
    FloatingActionButton fab;
    LinearLayout myGallery;
    MenuItem favoriteMenu;
    boolean favoriteFlag;

    /*
     * Información de Bluetooth
     */
    Beacon beacon;

    /*
     * Información Quemada para Imágenes
     */
    String galleryDirectoryName = "products";

    /*
     * Información de Base de Datos
     */
    BeaconDTO[] beaconsList;
    ZoneDTO zone;
    ProductDTO[] productList;
    ProductDTO mainProduct;

    private CustomerDTO customer;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        /*
         * Carga de Datos para Despliegue en Pantalla
         */
        //Beacon Encontrado
        beacon = getIntent().getParcelableExtra("ProductBeacon");
        Log.e("Beacon Final Producto: ", beacon.getMacAddress().toString());

        /*
         * Configuracion Interfaz Visual
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();

        tvProducto = (TextView) findViewById(R.id.producto);
        tvDescripcion = (TextView) findViewById(R.id.descripcion);
        tvPrecioConDescuento = (TextView) findViewById(R.id.precioConDescuento);
        tvPrecioOriginal = (TextView) findViewById(R.id.precioOriginal);
        tvPrecioOriginal.setPaintFlags(tvPrecioOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDescuento = (TextView) findViewById(R.id.descuento);
        setFloatingActionButton();
        displayImage = (ImageView) findViewById(R.id.productImage);
        myGallery = (LinearLayout) findViewById(R.id.myGallery);

        tvDescripcion.setText(beacon.getMacAddress().toString());

        /*
            Consulta de toda la lista de beacons
         */
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        databaseConnectivity.getBeaconsList(this);

        // Obtener dato de Usuario
        // Obtener MAC y Confirmar Existencia
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        databaseConnectivity.getCustomer(this, macAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        favoriteMenu = menu.findItem(R.id.product_like);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(getIntent().getParcelableExtra("ProductBeacon") != null){
        beacon = getIntent().getParcelableExtra("ProductBeacon");
        Log.e("Beacon Final RESUME: ", beacon.getMacAddress().toString());
        // Relacion con Vistas
        tvDescripcion.setText(beacon.getMacAddress().toString());

        // TODO Crear método para obtener detalles del producto asociado al BeaconDTO
        //getProductDetails(beacon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If App icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.product_like:
                // Favorite Icon
                if (!favoriteFlag) {
                    favoriteMenu.setIcon(R.drawable.ic_favorite_pressed);
                    favoriteFlag = true;
                } else {
                    favoriteMenu.setIcon(R.drawable.ic_favorite_unpressed);
                    favoriteFlag = false;
                }
                // TODO Publicar dato de like del producto en base de datos

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono y logo del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);
        }
        favoriteFlag = false;
    }

    private void setFloatingActionButton(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Publicar dato de compra/redención del producto en base de datos
                DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(ProductActivity.this);
                databaseConnectivity.createProductPurchase(ProductActivity.this,JsonMessageEncoder.encodeProductLike(customer.getId(),mainProduct.getId()));
            }
        });
    }

    @Override
    public void beaconsListResult(JSONObject jsonResult) {
        beaconsList = JsonResponseDecoder.beaconListResponse(jsonResult);
        for(BeaconDTO iteratorBeacon: beaconsList){
            if((iteratorBeacon.getUuid().equalsIgnoreCase(beacon.getProximityUUID().toString()))&&(iteratorBeacon.getMajor().equals(String.valueOf(beacon.getMajor())))&&(iteratorBeacon.getMinor().equals(String.valueOf(beacon.getMinor())))){
                DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
                databaseConnectivity.getZone(this,iteratorBeacon.getId());
                return;
            }
        }
    }

    @Override
    public void customerResult(JSONObject jsonResult) {
        customer = JsonResponseDecoder.customerResponse(jsonResult);
    }

    @Override
    public void zoneResult(JSONObject jsonResult) {
        /*
         * Consulta Listado de Productos
         */
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        zone = JsonResponseDecoder.zoneResponse(jsonResult);
        databaseConnectivity.getProductZoneList(this, zone.getId());

        // Interfaz Gráfica
        toolbar.setTitle(zone.getName());
    }

    @Override
    public void productsZoneList(JSONObject jsonResult) {
        productList = JsonResponseDecoder.productListResponse(jsonResult);

        // Elegir un producto aletorio de la lista
        Random random = new Random();
        int randomInteger = random.nextInt(productList.length);
        ProductDTO randomProduct = productList[randomInteger];

        // Publicar la información de ese producto en la pantalla
        loadProductInformation(randomProduct);

        // Guardar este como el producto principal
        mainProduct = randomProduct;

        // Cargar imagenes asociadas
        loadGallery(productList,randomProduct);

    }

    private void loadProductInformation(ProductDTO product){
        tvProducto.setText(product.getName());
        tvDescripcion.setText(product.getDescription());
        tvPrecioOriginal.setText("$"+product.getPrice());
        tvDescuento.setText(product.getDiscount()+"%");
        try {
            displayImage.setImageBitmap(BitmapFactory.decodeStream(getAssets().open(galleryDirectoryName+"/"+product.getLocalUri())));
        } catch (IOException e) {
            Log.e("GalleryScrollView", e.getMessage(), e);
        }
    }

    private void loadGallery(ProductDTO[] productList,ProductDTO selectedProduct){
        try {
            for (final ProductDTO iteratorProduct : productList) {
                if(iteratorProduct.getId()!= selectedProduct.getId()){
                    InputStream is = getAssets().open(galleryDirectoryName + "/" + iteratorProduct.getLocalUri());

                    final Drawable shownImage = Drawable.createFromStream(is,iteratorProduct.getLocalUri());
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(320, LinearLayout.LayoutParams.WRAP_CONTENT);
                    vp.gravity = Gravity.CENTER;
                    imageView.setLayoutParams(vp);
                    imageView.setMaxHeight(320);
                    imageView.setAdjustViewBounds(true);

                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    imageView.setImageDrawable(shownImage);
                    imageView.setMinimumWidth(imageView.getHeight());

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadProductInformation(iteratorProduct);
                            mainProduct = iteratorProduct;
                        }
                    });
                    myGallery.addView(imageView);
                }
            }
        } catch (IOException e) {
            Log.e("GalleryScrollView", e.getMessage(), e);
        }
    }

}
