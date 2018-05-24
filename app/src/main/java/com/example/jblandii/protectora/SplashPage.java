package com.example.jblandii.protectora;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashPage extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 3000;

    TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Orientacion de la pantalla
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Escondemos el titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_page);

        task = new TimerTask() {
            @Override
            public void run() {

                // Inicio la siguiente actividad
                Intent mainIntent = new Intent().setClass(
                        SplashPage.this, MainActivity.class);
                startActivity(mainIntent);

                // Cierro la actividad para que no este disponible si se utiliza el boton de
                // volver atras
                finish();
            }
        };

        // Simulacion de tiempo de carga
        if (validaPermisos()) {
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }
    }

    /**
     * Function que comprueba si tiene los permisos aceptados.
     *
     * @return
     */
    private boolean validaPermisos() {
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }

    /**
     * En el caso de que anteriormente haya denegado los permisos, se lo indico con esta funcion, y redirijo al usuario a ajustes, donde podrá activarlos manualmente.
     */
    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"SI", "NO"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(SplashPage.this);
        alertOpciones.setTitle("¿Desea configurar de forma manual los permisos?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("SI")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no han sido aceptados", Toast.LENGTH_LONG).show();
                    Timer timer = new Timer();
                    timer.schedule(task, SPLASH_SCREEN_DELAY);
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    /**
     * Indico que los permisos deben ser aceptados para el correcto funcionamiento de la aplicación.
     */
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(SplashPage.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 100);
            }
        });
        dialogo.show();
    }

    /**
     * En el caso de que tenga los permisos aceptados activo no solicito los permisos de forma manual.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (!(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                solicitarPermisosManual();
            } else {
                Timer timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);
            }
        }
    }
}
