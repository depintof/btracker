package com.btracker.test9;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {

    private Integer images[] = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,R.drawable.pic4};

    TextView tvProducto;
    TextView tvDescripcion;
    TextView tvPrecioConDescuento;
    TextView tvPrecioOriginal;
    TextView tvDescuento;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Relacion con Vistas
        tvProducto = (TextView) findViewById(R.id.producto);
        tvDescripcion = (TextView) findViewById(R.id.descripcion);
        tvPrecioConDescuento = (TextView) findViewById(R.id.precioConDescuento);
        tvPrecioOriginal = (TextView)findViewById(R.id.precioOriginal);
        tvDescuento = (TextView) findViewById(R.id.descuento);

        tvPrecioOriginal.setPaintFlags(tvPrecioOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setToolbar();

        // Note that Gallery view is deprecated in Android 4.1---
        Gallery gallery = (Gallery) findViewById(R.id.myGallery);
        if (gallery != null) {
            gallery.setAdapter(new ImageAdapter(this));
            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Toast.makeText(getBaseContext(), "pic" + (position + 1) + " selected",
                            Toast.LENGTH_SHORT).show();
                    // display the images selected
                    ImageView imageView = (ImageView) findViewById(R.id.productImage);
                    if (imageView != null) {
                        imageView.setImageResource(images[position]);
                    }
                }
            });
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return images.length;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        @SuppressWarnings("deprecation")
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.MATCH_PARENT));
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(0);
            imageView.setBackgroundResource(itemBackground);
            return imageView;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
