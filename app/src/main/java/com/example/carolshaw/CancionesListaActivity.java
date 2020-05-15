package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;

import org.json.JSONObject;

import java.util.ArrayList;

public class CancionesListaActivity extends AppCompatActivity {

    private ArrayList<Cancion> canciones = null;
    private String nombreLista;
    private TextView tituloVista;
    private ImageView botonBorrar;
    private RecyclerView recycler;
    private ResultadoCancionesBusquedaAdapter adapter;
    private String URL_API;
    private int idLista;
    private UsuarioDto usuarioLog;
    private int indiceLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones_lista);
        tituloVista = findViewById(R.id.tituloLista);
        botonBorrar = findViewById(R.id.btnBorrarLista);
        recycler = findViewById(R.id.recyclerViewCanciones);
        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getApplicationContext();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
            nombreLista = b.getString("nombre");
            idLista = b.getInt("idLista");
            indiceLista = b.getInt("indiceLista");
            tituloVista.setText(nombreLista);
            adapter = new ResultadoCancionesBusquedaAdapter(canciones);
            recycler.setLayoutManager(new LinearLayoutManager(CancionesListaActivity.this,
                    LinearLayoutManager.VERTICAL,false));
            recycler.setAdapter(adapter);
        }
        //Evita que se borre si es el de favoritos
        if (nombreLista.equals("Favoritos")) {
            Log.d("ModificarPerfil","no se borra");
            botonBorrar.setVisibility(View.GONE);
        }

        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarLista();
            }
        });
    }

    private void borrarLista() {
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        String url = URL_API + "/listaCancion/delete/" + idLista;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ModificarPerfil","indiceLista: " + indiceLista);
                        usuarioLog.deleteLista_cancion(indiceLista);
                        finish();
                        //startActivity(new Intent(CancionesListaActivity.this, ListaCancionesActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ModificarPerfil","indiceLista: " + indiceLista);
                        usuarioLog.deleteLista_cancion(indiceLista);
                        finish();
                        //startActivity(new Intent(CancionesListaActivity.this, ListaCancionesActivity.class));
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
}
