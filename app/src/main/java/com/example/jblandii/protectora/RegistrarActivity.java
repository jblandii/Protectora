package com.example.jblandii.protectora;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import static com.example.jblandii.protectora.ValidatorUtil.validateEmail;

public class RegistrarActivity extends AppCompatActivity {

    TextInputLayout til_login, til_contrasena, til_contrasena_confirmar, til_email, til_email_confirmar;
    TextInputEditText tie_login, tie_contrasena, tie_contrasena_confirmar, tie_email, tie_email_confirmar;
    Button btn_enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        cargarBotones();
    }

    private void cargarBotones() {
        btn_enviar = findViewById(R.id.btn_enviar);
        til_login = findViewById(R.id.til_login);
        til_contrasena = findViewById(R.id.til_contrasena);
        til_contrasena_confirmar = findViewById(R.id.til_contrasena_confirmar);
        til_email = findViewById(R.id.til_email);
        til_email_confirmar = findViewById(R.id.til_email_confirmar);
        tie_login = findViewById(R.id.tie_login);
        tie_contrasena = findViewById(R.id.tie_contrasena);
        tie_contrasena_confirmar = findViewById(R.id.tie_contrasena_confirmar);
        tie_email = findViewById(R.id.tie_email);
        tie_email_confirmar = findViewById(R.id.tie_email_confirmar);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tie_login.getText().toString().isEmpty()) {
                    til_login.setError(getResources().getString(R.string.mensaje_login_vacio));
                } else if (tie_email.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setError(getResources().getString(R.string.mensaje_email_vacio));
                } /*else if (validateEmail(tie_email.getText().toString())) {
                    til_login.setErrorEnabled(false);
                    til_email.setError(getResources().getString(R.string.mensaje_email_no_formato));
                }*/ else if (tie_email_confirmar.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setError(getResources().getString(R.string.mensaje_email_confirmar_vacio));
                } /*else if (validateEmail(tie_email_confirmar.getText().toString())) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    tie_email_confirmar.setError(getResources().getString(R.string.mensaje_email_no_formato));
                } */ else if (tie_contrasena.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setError(getResources().getString(R.string.mensaje_contrasena_vacia));
                } else if (tie_contrasena_confirmar.getText().toString().isEmpty()) {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setErrorEnabled(false);
                    til_contrasena_confirmar.setError(getResources().getString(R.string.mensaje_contrasena_confirmar_vacia));
                } else {
                    til_login.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_email_confirmar.setErrorEnabled(false);
                    til_contrasena.setErrorEnabled(false);
                    til_contrasena_confirmar.setErrorEnabled(false);
                    if (!tie_email.getText().toString().equals(tie_email_confirmar.getText().toString())) {
                        Snackbar.make(view, getResources().getString(R.string.mensaje_email_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (!tie_contrasena.getText().toString().equals(tie_contrasena_confirmar.getText().toString())) {
                        Snackbar.make(view, getResources().getString(R.string.mensaje_contrasenas_coincidir), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {

                    }
                }
            }
        });
    }
}
