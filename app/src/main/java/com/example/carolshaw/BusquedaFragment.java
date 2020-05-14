package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;


public class BusquedaFragment extends Fragment {

    private EditText barraBusqueda;
    private Button botonBusqueda;
    private String URL_API;
    private String busqueda;
    private Album albumBuscado = new Album();

    public BusquedaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        barraBusqueda = getView().findViewById(R.id.barraBusqueda);
        botonBusqueda = getView().findViewById(R.id.botonBusqueda);

        botonBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAlbum();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busqueda, container, false);
    }

    private void buscarAlbum(){
        busqueda = barraBusqueda.getText().toString();
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/album/getByTitulo?titulo=" + busqueda;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Artista artista = new Artista();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                albumBuscado.setId(response.getJSONObject(i).getInt("id"));
                                albumBuscado.setTitulo(response.getJSONObject(i).getString("titulo"));
                                albumBuscado.setCaratula(response.getJSONObject(i).getString("caratula"));
                                artista.setName(response.getJSONObject(i).getString("artista"));
                                albumBuscado.setArtista(artista);
                                //Coge canciones
                                JSONArray JSONCanciones = response.getJSONObject(i).getJSONArray("canciones");
                                ArrayList<Cancion> arrayCanciones = new ArrayList<Cancion>();
                                for(int j=0; j<JSONCanciones.length(); j++){
                                    Cancion cancion = new Cancion();
                                    cancion.setId(JSONCanciones.getJSONObject(j).getInt("id"));
                                    cancion.setName(JSONCanciones.getJSONObject(j).getString("name"));
                                    cancion.setFecha_subida(JSONCanciones.getJSONObject(j).getString("fecha_subida"));
                                    cancion.setDuracion(JSONCanciones.getJSONObject(j).getInt("duracion"));
                                    cancion.setAlbum(JSONCanciones.getJSONObject(j).getString("album"));
                                    cancion.setArtistas(JSONCanciones.getJSONObject(j).getString("artistas"));
                                    arrayCanciones.add(cancion);
                                }
                                albumBuscado.setCanciones(arrayCanciones);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new CancionesAlbumFragment().newInstance(albumBuscado)).commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error: " + error.toString());
                        Toast.makeText(getContext(), "Elemento no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }
}
