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
                        Log.d("LoginActivity", "exito validar (usuario existente): " + response.toString());
                        //Exito aquí significa que el usuario existía, por tanto no se debe permitir el registro
                        try {
                            UsuarioDto usuarioLog = (UsuarioDto) getApplicationContext();
                            usuarioLog.setId(response.getInt("id"));
                            usuarioLog.setNombre(response.getString("nombre"));
                            usuarioLog.setApellidos(response.getString("apellidos"));
                            usuarioLog.setNick(response.getString("nick"));
                            usuarioLog.setContrasena(response.getString("contrasena"));
                            usuarioLog.setTipo_user(response.getBoolean("tipo_user"));
                            usuarioLog.setFecha_nacimiento(response.getString("fecha_nacimiento"));
                            JSONArray JSONListaCanciones = response.getJSONArray("lista_cancion");
                            ArrayList<ListaCancion> arrayListaCancion = new ArrayList<ListaCancion>();
                            for (int i = 0; i < JSONListaCanciones.length(); i++) {
                                ListaCancion listaCanciones = new ListaCancion();
                                listaCanciones.setId(JSONListaCanciones.getJSONObject(i).getInt("id"));
                                listaCanciones.setId_usuario(JSONListaCanciones.getJSONObject(i).getInt("id_usuario"));
                                listaCanciones.setNombre(JSONListaCanciones.getJSONObject(i).getString("nombre"));
                                JSONArray JSONcanciones = JSONListaCanciones.getJSONObject(i).getJSONArray("canciones");
                                for (int j = 0; i < JSONcanciones.length(); i++) {
                                    Gson gson = new Gson();
                                    Cancion obj = gson.fromJson(JSONcanciones.getJSONObject(j).toString(), Cancion.class);
                                    listaCanciones.addCancion(obj);
                                }
                                arrayListaCancion.add(listaCanciones);
                            }
                            usuarioLog.setLista_cancion(arrayListaCancion);
                            if(!response.isNull("id_ultima_reproduccion")){
                                usuarioLog.setId_ultima_reproduccion(Integer.parseInt(response.getString("id_ultima_reproduccion")));
                            }
                            if(!response.isNull("minuto_ultima_reproduccion")) {
                                usuarioLog.setMinuto_ultima_reproduccion(response.getInt("minuto_ultima_reproduccion"));
                            }
                            if(!response.isNull("tipo_ultima_reproduccion")){
                                usuarioLog.setTipo_ultima_reproduccion(response.getInt("tipo_ultima_reproduccion"));
                            }
                            //FALTA POR AÑADIR LISTA_CANCION Y AMIGOS
                            iniciarMainLogged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
