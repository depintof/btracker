package com.innovamos.btracker.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.innovamos.btracker.R;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private Context context;
    private static Bitmap loading;
    private Bitmap imageBitmap;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        loading = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_picture);
        bmImage.setImageBitmap(loading);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        imageBitmap = null;

        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            imageBitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "Image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return imageBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
