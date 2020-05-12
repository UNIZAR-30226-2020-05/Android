package com.example.carolshaw;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.adapters.PanelSocialAdapter;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.UsuarioDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class ListaCancionesActivity extends AppCompatActivity {

    private UsuarioDto usuarioLog;
    private String URL_API ;
    private String nombreLista = "";
    private RecyclerView recycler;
    private ListaCancionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canciones);
        usuarioLog = (UsuarioDto) getApplicationContext();
        URL_API = getString(R.string.API);
        recycler = findViewById(R.id.recyclerViewCanciones);
        adapter = new ListaCancionesAdapter(usuarioLog.getLista_cancion());

        cargarListas();
    }

    //Carga las listas del usuario
    private void cargarListas() {
        recycler.setLayoutManager(new LinearLayoutManager(ListaCancionesActivity.this,
                LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(adapter);
    }

    //Función del botón añadir nueva lista
    public void nuevaLista(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombreLista = input.getText().toString();
                crearNuevaLista();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Manda la petición al back para crear la lista
    private void crearNuevaLista() {
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        String urlPost = URL_API + "/listaCancion/create";
        // Adding parameters to request
        try {
            params.put("id_usuario", usuarioLog.getId());
            params.put("nombre", nombreLista);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlPost, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ListaCancionesActivity", "response: " + response.toString());
                        try {
                            ListaCancion listaCancion = new ListaCancion(response.getInt("id"),
                                    response.getInt("id_usuario"),response.getString("nombre"));
                            usuarioLog.addLista_cancion(listaCancion);
                            cargarListas();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ListaCancionesActivity", "error: " + error.toString());
                        Toast.makeText(ListaCancionesActivity.this, "Error al cargar las listas de canción", Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
}
