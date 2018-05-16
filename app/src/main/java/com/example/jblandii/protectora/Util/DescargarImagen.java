package com.example.jblandii.protectora.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by jblandii on 15/05/18.
 */

public class DescargarImagen {

    public static Bitmap descargarImagen(String url) {
        Descarga descarga = new Descarga();
        descarga.execute(url);
        Bitmap bitmap = null;
        try {
            bitmap = descarga.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;

    }


    private static class Descarga extends AsyncTask<String, Void, Bitmap> {
        URL newurl;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap mIcon_val = null;
            try {
                newurl = new URL(params[0]);
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mIcon_val;
        }
    }
}