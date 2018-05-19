package com.example.jblandii.protectora.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public static void guardaImagen(Bitmap b, String ruta, String picName) {
        if (b != null) {
            crearCarpeta(ruta);
            FileOutputStream fos;
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/Mimame/" + ruta + picName);
            try {
                fos = new FileOutputStream(f);
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                // Log.d("IMAGEN NO ENCONTRADA", "file not found");
                e.printStackTrace();
            } catch (IOException e) {
                // Log.d("IO", "io exception");
                e.printStackTrace();
            }
        }
    }

    public static Bitmap comprobarImagen(String ruta, String picName) {
        File f = new File(Environment.getExternalStorageDirectory()
                + "/Mimame/" + ruta + picName);
        if (f.exists()) {
            Bitmap imagen_guardada = BitmapFactory.decodeFile(f.getAbsolutePath());
            return imagen_guardada;
        } else {
            return null;
        }
    }

    public static Bitmap borrarImagen(String ruta, String picName) {
        File f = new File(Environment.getExternalStorageDirectory()
                + "/Mimame/" + ruta + picName);
        if (f.exists()) {
            Bitmap imagen_guardada = BitmapFactory.decodeFile(f.getAbsolutePath());
            return imagen_guardada;
        } else {
            return null;
        }
    }

    public static void crearCarpeta(String ruta) {
        String folder_main = "Mimame/";
        File f = new File(Environment.getExternalStorageDirectory() + "/"
                + folder_main, ruta);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}