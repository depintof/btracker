package com.btracker.test9;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;

import java.io.IOException;
import java.io.InputStream;

public class ProductActivity extends AppCompatActivity {

    //private Integer images[] = {R.drawable.gafas, R.drawable.gorra, R.drawable.pantalon,R.drawable.pic4};

    TextView tvProducto;
    TextView tvDescripcion;
    TextView tvPrecioConDescuento;
    TextView tvPrecioOriginal;
    TextView tvDescuento;
    ImageView displayImage;
    LinearLayout myGallery;
    MenuItem favoriteMenu;
    com.estimote.sdk.Beacon beacon;
    boolean favoriteFlag;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Beacon de notificación
        beacon = getIntent().getParcelableExtra("ProductBeacon");
        // TODO Crear método para obtener detalles del producto asociado al Beacon
        //getProductDetails(beacon);

        // Relacion con Vistas
        tvProducto = (TextView) findViewById(R.id.producto);
        tvDescripcion = (TextView) findViewById(R.id.descripcion);
        tvDescripcion.setText(beacon.getMacAddress().toString());
        Log.e("Beacon Final Producto: ", beacon.getMacAddress().toString());

        tvPrecioConDescuento = (TextView) findViewById(R.id.precioConDescuento);
        tvPrecioOriginal = (TextView) findViewById(R.id.precioOriginal);
        tvDescuento = (TextView) findViewById(R.id.descuento);

        displayImage = (ImageView) findViewById(R.id.productImage);
        myGallery = (LinearLayout) findViewById(R.id.myGallery);

        tvPrecioOriginal.setPaintFlags(tvPrecioOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setToolbar();

        //setGallery();

        try {
            String galleryDirectoryName = "gallery";
            String[] listImages = getAssets().list(galleryDirectoryName);
            for (String imageName : listImages) {
                InputStream is = getAssets().open(galleryDirectoryName + "/" + imageName);
                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
                //imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setAdjustViewBounds(true);
                //imageView.setMaxHeight(0);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displayImage.setImageBitmap(bitmap);
                    }
                });

                myGallery.addView(imageView);
            }
        } catch (IOException e) {
            Log.e("GalleryScrollView", e.getMessage(), e);
        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono y logo del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);
        }
        favoriteFlag = false;
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
        // TODO Crear método para obtener detalles del producto asociado al Beacon
        //getProductDetails(beacon);

        // Relacion con Vistas
        tvDescripcion.setText(beacon.getMacAddress().toString());
        Log.e("Beacon Final RESUME: ", beacon.getMacAddress().toString());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.product_like:
                // app icon in action bar clicked; goto parent activity.
                if (!favoriteFlag) {
                    favoriteMenu.setIcon(R.drawable.ic_favorite_pressed);
                    favoriteFlag = true;
                } else {
                    favoriteMenu.setIcon(R.drawable.ic_favorite_unpressed);
                    favoriteFlag = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
