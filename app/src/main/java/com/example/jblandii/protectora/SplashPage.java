package com.example.jblandii.protectora;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashPage extends AppCompatActivity {

//    private ImageView imageLogo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_page);
//
//        imageLogo = findViewById(R.id.ivLogo);
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
//        imageLogo.startAnimation(anim);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashPage.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },3000);
//    }

    // Duracion del Splash
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Orientacion de la pantalla
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Escondemos el titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_page);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Iniciamos la siguiente actividad
                Intent mainIntent = new Intent().setClass(
                        SplashPage.this, MainActivity.class);
                startActivity(mainIntent);

                // Cerramos la actividad para que no este disponible si se utiliza el boton de
                // volver atras
                finish();
            }
        };

        // Simulacion de tiempo de carga
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
