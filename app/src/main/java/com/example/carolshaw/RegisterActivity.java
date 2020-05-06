package com.example.carolshaw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView alias;
    private TextView nombre;
    private TextView apellidos;
    private TextView password;
    private TextView fecha;
    private Button registrar;
    private String URL_API = "http://192.168.1.121:8080";

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

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario user = new Usuario("nombre", "apellios", "alias",
                        "contra", "12/06/1997");

            }
        });


    }
}
