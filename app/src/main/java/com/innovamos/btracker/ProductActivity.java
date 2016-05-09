package com.innovamos.btracker;

import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.dto.CustomerProductsDTO;
import com.innovamos.btracker.dto.ProductDTO;
import com.innovamos.btracker.dto.ZoneDTO;
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
    boolean favoriteFlag = false;

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
    CustomerProductsDTO[] productsLikeList;

    private String customerId;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        /*
         * Carga de Datos para Despliegue en Pantalla
         */
        //Beacon Encontrado
        Bundle extras = getIntent().getExtras();
        beacon = extras.getParcelable("ProductBeacon");
        customerId = extras.getString("Customer");
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

        /*
            Consulta de toda la lista de productos con like
         */
        databaseConnectivity.getProductsLike(this, customerId);

        // Obtener dato de Usuario
        // Obtener MAC y Confirmar Existencia
        //databaseConnectivity.getCustomer(this, MainActivity.getMacAddr());
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
        if(getIntent().getParcelableExtra("Customer") != null){
            customerId = getIntent().getParcelableExtra("Customer");
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
                DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
                if(favoriteFlag){
                    databaseConnectivity.deleteProductLike(this,customerId,mainProduct.getId());
                }
                else{
                    // TODO Publicar dato de like del producto en base de datos
                    databaseConnectivity.createProductLike(this, customerId, mainProduct.getId());
                }
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
                //TODO publicar compra del producto
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
        //customer = JsonResponseDecoder.customerResponse(jsonResult);
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

        for(CustomerProductsDTO iterator: productsLikeList){
            if(iterator.getProduct_id().equals(mainProduct.getId())){
                favoriteMenu.setIcon(R.drawable.ic_favorite_pressed);
                favoriteFlag = true;
            }
        }
        // Favorite Icon
        if (!favoriteFlag) {
            favoriteMenu.setIcon(R.drawable.ic_favorite_unpressed);
            favoriteFlag = false;
        }

        // Cargar imagenes asociadas
        loadGallery(productList, randomProduct);
    }

    @Override
    public void productsLikeList(JSONObject jsonResult) {
        productsLikeList = JsonResponseDecoder.productsLikeListResponse(jsonResult);

    }

    @Override
    public void insertProductLike(JSONObject jsonResult) {
        if(JsonResponseDecoder.insertProductLikeResponse(jsonResult)!=null){
            Toast.makeText(this,"Este producto se agregó a tu lista de favoritos",Toast.LENGTH_LONG).show();
            favoriteMenu.setIcon(R.drawable.ic_favorite_pressed);
            favoriteFlag = true;
        }
        else{
            Toast.makeText(this,"Algo ocurrió mal",Toast.LENGTH_SHORT).show();
            favoriteMenu.setIcon(R.drawable.ic_favorite_unpressed);
            favoriteFlag = false;
        }
    }

    @Override
    public void deleteProductLike(JSONObject jsonResult) {
        if(JsonResponseDecoder.deleteProductLikeResponse(jsonResult)!=null){
            Toast.makeText(this,"Se eliminó el producto de tu lista de favoritos",Toast.LENGTH_SHORT).show();
            favoriteMenu.setIcon(R.drawable.ic_favorite_unpressed);
            favoriteFlag = false;
        }
        else{
            Toast.makeText(this,"Algo ocurrió mal",Toast.LENGTH_SHORT).show();
            favoriteMenu.setIcon(R.drawable.ic_favorite_pressed);
            favoriteFlag = true;
        }
    }

    private void loadProductInformation(ProductDTO product){
        tvProducto.setText(product.getName());
        tvDescripcion.setText(product.getDescription());
        tvPrecioOriginal.setText("$"+product.getPrice());
        tvDescuento.setText(product.getDiscount()+"%");
        try {
            displayImage.setImageBitmap(BitmapFactory.decodeStream(getAssets().open(galleryDirectoryName+"/"+product.getPicture())));
        } catch (IOException e) {
            Log.e("GalleryScrollView", e.getMessage(), e);
        }
    }

    private void loadGallery(ProductDTO[] productList,ProductDTO selectedProduct){
        try {
            for (final ProductDTO iteratorProduct : productList) {
                if(iteratorProduct.getId()!= selectedProduct.getId()){
                    InputStream is = getAssets().open(galleryDirectoryName + "/" + iteratorProduct.getPicture());

                    final Drawable shownImage = Drawable.createFromStream(is,iteratorProduct.getPicture());
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
