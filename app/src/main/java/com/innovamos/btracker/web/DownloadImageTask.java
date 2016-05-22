package com.innovamos.btracker.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.innovamos.btracker.MainActivity;
import com.innovamos.btracker.R;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ProgressDialog mDialog;
    private ImageView bmImage;
    private Context context;

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
    }

    protected void onPreExecute() {
        //mDialog = ProgressDialog.show(this.context, "Por favor espere...", "Obteniendo información...", true);
        Bitmap loading = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_picture);
        bmImage.setImageBitmap(loading);
    }


    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", "image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
        bmImage.setImageBitmap(result);
        //close
        // mDialog.dismiss();
    }
}
