package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private TextView userRegistration;
    private Button loginButton;
    private TextView nick;
    private TextView contrasena;
    private String URL_API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        URL_API = getString(R.string.API);

        userRegistration = findViewById(R.id.tvRegister);
        loginButton = findViewById(R.id.btnLogin);
        nick = findViewById(R.id.etName);
        contrasena = findViewById(R.id.etPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlGet = URL_API + "/user/logIn?nick=" + nick.getText().toString() +
                    "&pass=" + contrasena.getText().toString();
                comprobarUsuario(urlGet);
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void comprobarUsuario (String urlGet){
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LoginActivity", "exito validar (usuario existente): " + response.toString());
                        //Exito aquí significa que el usuario existía, por tanto no se debe permitir el registro
                        iniciarMainLogged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LoginActivity", "error validar (usuario no existente): " + error.toString());
                        informarUsuarioDesconocido();
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    /* Inicia la actividad MainLogged
     */
    private void iniciarMainLogged() {
        startActivity(new Intent(LoginActivity.this, MainLogged.class));
    }

    /* informa mediante un TOAST de que el usuario ya existe en la bbdd
     */
    private void informarUsuarioDesconocido() {
        Toast toast = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
