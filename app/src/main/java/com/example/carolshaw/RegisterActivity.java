package com.example.carolshaw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.UserRequest;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {
    public TextView userLogin;
    private TextView nick;
    private TextView nombre;
    private TextView apellidos;
    private TextView contrasena;
    private TextView fecha;
    private Button registrar;
    private TextView volverLogin;
    private String URL_API;

    Boolean valido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        URL_API = getString(R.string.API);

        userLogin = findViewById(R.id.tvUserLogIn);
        registrar = findViewById(R.id.btnRegister);
        //Datos del usuario
        nick = findViewById(R.id.Alias);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        contrasena = findViewById(R.id.password);

        fecha = findViewById(R.id.fecha);

        //Boton registrar
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlGet = URL_API + "/user/get?nick=" + nick.getText().toString();
                if(comprobarCamposRellenos()) {
                    validar(urlGet);
                } else {
                    informarRellenarDatos();
                }
            }
        });

        volverLogin = findViewById(R.id.tvUserLogIn);

        volverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    /* Comprueba que todos los campos de la interfaz han sido rellenados
     */
    private Boolean comprobarCamposRellenos() {
        Boolean valido;

        if(nombre.getText().toString().isEmpty() || apellidos.getText().toString().isEmpty() ||
            nick.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty() ||
            fecha.getText().toString().isEmpty()){
            valido = false;
        } else {
            valido = true;
        }
        return valido;
    }

    /* Comprueba que el usuario no existe en la bbdd, en caso de existir, informa mediante un toast
     * y si no existe, procede a registrar al usuario
     */
    private void validar(String urlGet) {
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RegisterActivity", "exito validar (usuario repetido): " + response.toString());
                        //Exito aquí significa que el usuario existía, por tanto no se debe permitir el registro
                        informarUsuarioExistente();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RegisterActivity", "error validar (usuario no repetido): " + error.toString());
                        registrarUsuario();
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    /* informa mediante un TOAST de que el usuario ya existe en la bbdd
     */
    private void informarUsuarioExistente() {
        Toast toast = Toast.makeText(getApplicationContext(), "Ese nick no está disponible", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
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

    /* Registra al usuario con los campos de la interfaz
     */
    private void registrarUsuario() {
        UserRequest user = new UserRequest(nombre.getText().toString(), apellidos.getText().toString(),
                nick.getText().toString(), contrasena.getText().toString(), fecha.getText().toString());


        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        // Adding parameters to request
        try {
            String contrasenaCifr = (android.util.Base64.encodeToString(user.getContrasena().getBytes(),
                    android.util.Base64.DEFAULT));
            params.put("nombre", user.getNombre());
            params.put("apellidos", user.getApellidos());
            params.put("nick", user.getNick());
            params.put("contrasena", contrasenaCifr.substring(0, contrasenaCifr.length()-1));
            params.put("tipo_user", true);

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
                        Log.d("RegisterActivity", "exito: " + response.toString());
                        Toast.makeText(RegisterActivity.this, "Registro realizado correctamente", Toast.LENGTH_SHORT).show();
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
                            }
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
                            startActivity(new Intent(RegisterActivity.this, MainLogged.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RegisterActivity", "error: " + error.toString());
                        Toast.makeText(RegisterActivity.this, "Error desconocido al registrar", Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
}
