package com.example.carolshaw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.UserRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView nick;
    private TextView nombre;
    private TextView apellidos;
    private TextView contrasena;
    private TextView fecha;
    private Button registrar;
    private String URL_API;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        URL_API =  getString(R.string.API);

        userLogin = findViewById(R.id.tvUserLogIn);
        registrar = findViewById(R.id.btnRegister);
        //Datos del usuario
        nick = findViewById(R.id.Alias);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        contrasena = findViewById(R.id.password);
        fecha = findViewById(R.id.fecha);

        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        //Boton registrar
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRequest user = new UserRequest(nombre.getText().toString(), apellidos.getText().toString(),
                        nick.getText().toString(), contrasena.getText().toString(), fecha.getText().toString());

                if (validar() && false) {
                    JSONObject params = new JSONObject();
                    // Adding parameters to request
                    try {
                        //params.put("nuevo", user);
                        params.put("nombre", user.getNombre());
                        params.put("apellidos", user.getApellidos());
                        params.put("nick", user.getNick());
                        params.put("contrasena", user.getContrasena());
                        params.put("tipo_user", user.getTipo_user());

                        //Hay que pasar la fecha parseada y en formato long
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date date;
                        date = df.parse(fecha.getText().toString());
                        params.put("fecha_nacimiento", date.getTime());
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }

                    // Creating a JSON Object request
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_API + "/user/create", params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("response", "exito: " + response.toString());
                                    // other stuff ...
                                    Toast.makeText(RegisterActivity.this, "Registro realizado correctamente", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainLogged.class));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("response", "error: " + error.toString());
                                }
                            });

                    // Adding the string request to the queue
                    rq.add(jsonObjectRequest);
                }
            }
        });
    }
    private boolean validar() {
        Boolean valido = false;
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        JSONObject params = new JSONObject();
        // Adding parameters to request
        try {
            params.put("nick", "sam10");
            params.put("pass", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_API + "/user/get?nick=smm", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "exito validar: " + response.toString());
                        // other stuff ...
                        Toast.makeText(RegisterActivity.this, "get correcto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainLogged.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response", "error validar: " + error.toString());
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);

        return valido;
    }
}
