package com.example.jblandii.protectora.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jblandii.protectora.Models.Tarea;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.Util.DescargarImagen;
import com.example.jblandii.protectora.peticionesBD.Tags;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jblandii on 19/05/18.
 */

public class AdaptadorDetallesProtectora_ViewPager extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> imagenes;
    private Handler puente;
    ArrayList<Tarea> arrayTareas = new ArrayList<>();
    ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    public AdaptadorDetallesProtectora_ViewPager(Context context, ArrayList<String> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
        puente = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    Tarea tarea = ((Tarea) msg.obj);
                    ImageView imageView = (ImageView) tarea.getDato2();
                    imageView.setTag("descargado");
                    Bitmap bitmap = (Bitmap) tarea.getDatoExtra();
                    imageView.setImageBitmap(bitmap);
                }
            }
        };
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.detalles_protectora_view_pager, null);
        ImageView iv_imagen_protectora = view.findViewById(R.id.iv_imagen_protectora);
        iv_imagen_protectora.setImageResource(R.drawable.nueva_pancarta);
        arrayTareas.add(new Tarea("descargarFoto", imagenes.get(position), iv_imagen_protectora));
        hacerTarea();
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    private void hacerTarea() {
        pool.execute(new Thread() {
            @Override
            public void run() {
                while (arrayTareas.size() > 0) {
                    if (arrayTareas.get(0).getFuncion().contains("descargarFoto")) {
                        Bitmap bmp = descargarFoto(arrayTareas.get(0).getDato());
                        arrayTareas.get(0).setDatoExtra(bmp);
                        Message msg = new Message();
                        msg.what = 100;
                        msg.obj = arrayTareas.get(0);
                        puente.sendMessage(msg);
                    }
                    arrayTareas.remove(0);
                }
            }
        });
    }

    private Bitmap descargarFoto(Object objeto) {
        Bitmap bitmap;
        int longitud = objeto.toString().length();
        String url = objeto.toString();
        if (longitud > 0) {
            bitmap = DescargarImagen.descargarImagen(Tags.SERVIDOR + Tags.MEDIA + url);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        }
        return bitmap;
    }
}
