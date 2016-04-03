package com.btracker.test9;

import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    TextView tvProducto;
    TextView tvDescripcion;
    TextView tvPrecioConDescuento;
    TextView tvPrecioOriginal;
    TextView tvDescuento;

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
