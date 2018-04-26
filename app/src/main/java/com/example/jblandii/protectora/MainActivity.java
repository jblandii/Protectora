package com.example.jblandii.protectora;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jblandii.protectora.Models.Usuario;
import com.example.jblandii.protectora.peticionesBD.JSONUtil;
import com.example.jblandii.protectora.peticionesBD.Tags;

import com.example.jblandii.protectora.fragments.AnimalFragment;
import com.example.jblandii.protectora.fragments.ProtectoraFragment;
import com.example.jblandii.protectora.fragments.MeGustaFragment;
import com.example.jblandii.protectora.fragments.MensajeFragment;
import com.example.jblandii.protectora.fragments.ConfiguracionFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Fragment protectoraFragment = new ProtectoraFragment();
        final Fragment animalFragment = new AnimalFragment();
        final Fragment meGustaFragment = new MeGustaFragment();
        final Fragment mensajeFragment = new MensajeFragment();
        final Fragment configuracionFragment = new ConfiguracionFragment();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, animalFragment).commit();
        }

        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
//                switch (item.getItemId()) {
//                    case R.id.navigation_animales:
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentContainer, animalFragment).commit();
//                        break;
//                    case R.id.navigation_protectoras:
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentContainer, protectoraFragment).commit();
//                        break;
//                    case R.id.navigation_megustas:
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentContainer, meGustaFragment).commit();
//                        break;
//                    case R.id.navigation_mensajes:
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentContainer, mensajeFragment).commit();
//                        break;
//                    case R.id.navigation_configuracion:
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentContainer, configuracionFragment).commit();
//                        break;
//                }
                if (item.getItemId() == R.id.navigation_animales) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, animalFragment).commit();
                } else if (item.getItemId() == R.id.navigation_protectoras) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, protectoraFragment).commit();
                } else if (item.getItemId() == R.id.navigation_megustas) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, meGustaFragment).commit();
                } else if (item.getItemId() == R.id.navigation_mensajes) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, mensajeFragment).commit();
                } else if (item.getItemId() == R.id.navigation_configuracion) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, configuracionFragment).commit();
                }
                return true;
            }
        });


        /*Hacer que el teclado se superponga y no se mueva*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onResume() {
        super.onResume();
        lanzarLogin();
    }

    /**
     * Metodo que se encarga de lanzar el login.
     */
    public void lanzarLogin() {
        if (hayInternet()) {
            String token = Usuario.getToken(getApplicationContext());
            if (token == null || token == "" || !JSONUtil.compruebaToken(token)) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, Tags.LOGIN);
            }
        } else {
            Toast.makeText(MainActivity.this, Tags.SIN_CONEXION_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Metodo que utilizo para cargar los animales de la provincia a la que pertenece el usuario.
     */
    public void cargarAnimales() {
        if (hayInternet()) {
            String token = Usuario.getToken(getApplicationContext());
            if (token == null || token == "" || !JSONUtil.compruebaToken(token)) {

            }
        } else {
            Toast.makeText(MainActivity.this, Tags.SIN_CONEXION_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Configuro que al pulsar el botón de atras se salga de la aplicación pero que siga funcionando en 2º plano.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        moveTaskToBack(true);
        startActivity(intent);
    }

    /**
     * Metodo que se encarga de comprobar si hay conexión a internet.
     *
     * @return true en caso de haber conexión o false en el caso contrario.
     */
    public boolean hayInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
