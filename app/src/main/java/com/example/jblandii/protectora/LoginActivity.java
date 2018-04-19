package com.example.jblandii.protectora;

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

            }
        });

    }
}
