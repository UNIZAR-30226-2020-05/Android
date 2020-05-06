package com.example.carolshaw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.User;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView alias;
    private TextView nombre;
    private TextView apellidos;
    private TextView password;
    private TextView fecha;
    private Button registrar;
    private String URL_API = "http://3.22.247.114:8080";

    private RequestQueue queue;


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

        queue = Volley.newRequestQueue(this);

        /*Map<String, Object> params = new ArrayMap<>(2);
        // Adding parameters to request
        params.put(email, email);
        params.put(Config.KEY_PASSWORD, password);

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG, response.toString());
                        // other stuff ...
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // You can handle the error here if you want
                    }
                });

        // Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        queue.add(postRequest);
        */

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(nombre.getText().toString(), apellidos.getText().toString(),
                        alias.getText().toString(),password.getText().toString(), fecha.getText().toString());


            }
        });

    }
}
