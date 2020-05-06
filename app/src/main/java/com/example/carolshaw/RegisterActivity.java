package com.example.carolshaw;

import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.UserRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView nick;
    private TextView nombre;
    private TextView apellidos;
    private TextView contrasena;
    private TextView fecha;
    private Button registrar;
    private String URL_API = "http://3.22.247.114:8080";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userLogin = findViewById(R.id.tvUserLogIn);
        registrar = findViewById(R.id.btnRegister);
        //Datos del usuario
        nick = findViewById(R.id.Alias);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        contrasena = findViewById(R.id.password);
        fecha = findViewById(R.id.fecha);

        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRequest user = new UserRequest(nombre.getText().toString(), apellidos.getText().toString(),
                        nick.getText().toString(), contrasena.getText().toString(), fecha.getText().toString());

                JSONObject params = new JSONObject();
                // Adding parameters to request
                try {
                    params.put("nuevo", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creating a JSON Object request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_API + "/user/create", params,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                Log.d("response", "exito: " + response.toString());
                                // other stuff ...
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Log.d("response", "error: " + error.toString());
                            }
                        });

                // Adding the string request to the queue
                rq.add(jsonObjectRequest);
            }
        });

    }
}
