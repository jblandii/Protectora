package com.example.jblandii.protectora.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jblandii.protectora.DetallesAnimal;
import com.example.jblandii.protectora.Models.Animal;
import com.example.jblandii.protectora.Models.Tarea;
import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.R;
import com.example.jblandii.protectora.Util.DescargarImagen;
import com.example.jblandii.protectora.Util.Utilidades;
import com.example.jblandii.protectora.fragments.AnimalFragment;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Preferencias;
import com.example.jblandii.protectora.peticionesBD.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jblandii on 28/04/18.
 */

public class AdaptadorAnimales extends RecyclerView.Adapter<AdaptadorAnimales.AnimalesViewHolder> {

    ArrayList<Animal> listaAnimales;
    private Context context;
    private Handler puente;
    ArrayList<Tarea> arrayTareas = new ArrayList<>();
    ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);


    public AdaptadorAnimales(ArrayList<Animal> listaAnimales, Context context) {
        this.listaAnimales = listaAnimales;
        this.context = context;
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
    public AnimalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_animales, null, false);
        RecyclerView.LayoutParams layParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layParams);
        return new AnimalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnimalesViewHolder holder, final int position) {
        holder.tv_raza_animal.setText(listaAnimales.get(position).getRaza());
        holder.tv_nombre_animal.setText(listaAnimales.get(position).getNombre());
        holder.iv_animal.setImageResource(R.drawable.nueva_pancarta);
        if (holder.iv_animal.getTag().equals("logo")) {
            holder.iv_animal.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.iv_animal.setImageResource(R.drawable.logo);
            arrayTareas.add(new Tarea("descargarFoto", listaAnimales.get(position), holder.iv_animal));
            hacerTarea(position);
        }
        holder.cv_animales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetallesAnimal = new Intent(v.getContext(), DetallesAnimal.class);
                intentDetallesAnimal.putExtra("animal", (Parcelable) listaAnimales.get(position));
                v.getContext().startActivity(intentDetallesAnimal);
            }
        });

        if (listaAnimales.get(position).getMe_gusta().equals("true")) {
            holder.ib_megusta.setImageResource(R.drawable.ic_megusta);
        }

        holder.ib_megusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dar_mg(position)) {
                    if (listaAnimales.get(position).getMe_gusta().equals("true")) {
                        holder.ib_megusta.setImageResource(R.drawable.ic_megusta_borde);
                        listaAnimales.get(position).setMe_gusta("false");
                        DescargarImagen.borrarImagen("favoritos/", "animal" + listaAnimales.get(position).getPk());
                    } else {
                        holder.ib_megusta.setImageResource(R.drawable.ic_megusta);
                        listaAnimales.get(position).setMe_gusta("true");
                        holder.iv_animal.buildDrawingCache();
                        Bitmap bitmap = holder.iv_animal.getDrawingCache();
                        DescargarImagen.guardaImagen(bitmap, "favoritos/", "animal" + listaAnimales.get(position).getPk());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAnimales.size();
    }

    private void hacerTarea(final int position) {
        pool.execute(new Thread() {
            @Override
            public void run() {
                while (arrayTareas.size() > 0) {
                    if (arrayTareas.get(0).getFuncion().contains("descargarFoto")) {
                        Bitmap bmp;
                        if (!(listaAnimales.get(position).getMe_gusta()).equals("true")) {
                            bmp = descargarFoto((Animal) arrayTareas.get(0).getDato(), -1000);
                        } else {
                            bmp = descargarFoto((Animal) arrayTareas.get(0).getDato(), ((Animal) arrayTareas.get(0).getDato()).getPk());
                        }
                        int pk = ((Animal) arrayTareas.get(0).getDato()).getPk();
                        arrayTareas.get(0).setDatoExtra(bmp);
                        Message msg = new Message();
                        msg.what = 100;
                        msg.arg1 = Integer.parseInt(pk + "");
                        msg.obj = arrayTareas.get(0);
                        puente.sendMessage(msg);
                    }
                    arrayTareas.remove(0);
                }
            }
        });
    }

    private Bitmap descargarFoto(Object objeto, int pk) {
        Bitmap bitmap;
        int longitud = ((Animal) objeto).getImagenURL().length();
        String url = ((Animal) objeto).getImagenURL();
        if (longitud > 0) {
            bitmap = DescargarImagen.descargarImagen(Tags.SERVIDOR + url);
            if (pk != -1000) {
                DescargarImagen.guardaImagen(bitmap, "favoritos/", "animal" + pk);
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        }
        return bitmap;
    }

    /**
     * Metodo que utilizo para dar o quitar me gustas a los animales.
     * @param position
     * @return
     */
    public boolean dar_mg(int position) {
        //Creamos el JSON que vamos a mandar al servidor
        JSONObject json = new JSONObject();
        try {
            json.put(Tags.USUARIO_ID, Usuario.getID(context));
            json.put(Tags.TOKEN, Usuario.getToken(context));
            json.put(Tags.ANIMAL, listaAnimales.get(position).getPk());
            json.put(Tags.MEGUSTA, listaAnimales.get(position).getMe_gusta());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Se hace petición de login al servidor. */
        json = JSONUtil.hacerPeticionServidor("protectora/dar_mg/", json);

        try {
            String p = json.getString(Tags.RESULTADO);

            /* Se comprueba la conexión al servidor. */
            if (p.contains(Tags.ERRORCONEXION)) {
//                mensaje = "Error de conexión";
                return false;
            }
            /* En caso de que conecte */
            else if (p.contains(Tags.OK)) {
                return true;
            }

            /* Resultado falla por otro error. */
            else if (p.contains(Tags.ERROR)) {
                String msg = json.getString(Tags.MENSAJE);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class AnimalesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_raza_animal, tv_nombre_animal;
        ImageView iv_animal;
        ImageButton ib_megusta;
        CardView cv_animales;

        public AnimalesViewHolder(View itemView) {
            super(itemView);
            tv_raza_animal = itemView.findViewById(R.id.tv_raza_animal);
            tv_nombre_animal = itemView.findViewById(R.id.tv_nombre_animal);
            iv_animal = itemView.findViewById(R.id.iv_animal);
            ib_megusta = itemView.findViewById(R.id.ib_megusta);
            cv_animales = itemView.findViewById(R.id.cv_animales);
        }
    }
}