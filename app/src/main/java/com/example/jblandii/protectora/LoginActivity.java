package com.example.jblandii.protectora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jblandii.protectora.R;

public class LoginActivity extends AppCompatActivity {
    Button btn_iniciar_sesion, btn_registrar;
    TextView tv_recordar_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        cargarBotones();
    }

    /**
     * Metodo que utilizo para cargar los botones que tengo en el layout.
     */
    private void cargarBotones() {
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_registrar = findViewById(R.id.btn_registrar);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_recordar_contrasena = findViewById(R.id.tv_recordar_contrasena);
        tv_recordar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRecordarContrasena = new Intent(LoginActivity.this, RecordarContrasenaActivity.class);
                startActivity(intentRecordarContrasena);
            }
        });

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
}
