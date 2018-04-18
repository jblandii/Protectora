package com.example.jblandii.protectora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecordarContrasenaActivity extends AppCompatActivity {
    Button btn_recordar_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_contrasena);
        cargarBotones();
    }

    private void cargarBotones() {
        btn_recordar_contrasena = findViewById(R.id.btn_recordar_contrasena);
        btn_recordar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
