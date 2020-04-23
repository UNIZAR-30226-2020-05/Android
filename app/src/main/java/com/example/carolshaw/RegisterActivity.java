package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView alias;
    private TextView nombre;
    private TextView apellidos;
    private TextView password;
    private TextView fecha;
    private Button registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userLogin = findViewById(R.id.tvUserLogIn);
        registrar = findViewById(R.id.btnRegister);
        //Datos del usuario
        alias = findViewById(R.id.Alias);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        password = findViewById(R.id.password);
        fecha = findViewById(R.id.fecha);

    }

    public void cambiar(View v) {
        String kk = alias.getText().toString();
        Log.d("alias", kk);
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
