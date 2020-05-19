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
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64; // Para cifrados de pass.
import java.util.List;


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
                String urlGet = URL_API + "/user/logIn?nick=" + nick.getText().toString() + "&pass=" +
                        android.util.Base64.encodeToString(contrasena.getText().toString().getBytes(), android.util.Base64.DEFAULT);
                if(comprobarCamposRellenos()){
                    comprobarUsuario(urlGet);
                } else {
                    informarRellenarDatos();
                }
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
                        //Exito aquí significa que el usuario existía, por tanto no se debe permitir el registro
                        UsuarioDto usuarioLog = (UsuarioDto) getApplicationContext();
                        Gson gson = new Gson();
                        UsuarioDto obj = gson.fromJson(response.toString(), UsuarioDto.class);
                        usuarioLog.setUsuarioDto(obj);
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

    /* Comprueba que todos los campos de la interfaz han sido rellenados
     */
    private Boolean comprobarCamposRellenos() {
        Boolean valido;

        if(nick.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()){
            valido = false;
        } else {
            valido = true;
        }
        return valido;
    }

    /* informa mediante un TOAST de que falta algún campo por rellenar
     */
    private void informarRellenarDatos() {
        Toast toast = Toast.makeText(getApplicationContext(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
