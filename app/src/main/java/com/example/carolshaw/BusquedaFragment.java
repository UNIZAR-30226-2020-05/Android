package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;


public class BusquedaFragment extends Fragment {

    private EditText barraBusquedaCanciones;
    private Button botonBusquedaCanciones;
    private EditText barraBusquedaAlbumes;
    private Button botonBusquedaAlbumes;
    private EditText barraBusquedaArtistas;
    private Button botonBusquedaArtistas;
    private String URL_API;
    private String busqueda;
    private ArrayList<Album> albumesEncontrados = new ArrayList<Album>();
    private ArrayList<Cancion> cancionesEncontradas = new ArrayList<Cancion>();
    private ArrayList<Artista> artistasEncontrados = new ArrayList<Artista>();

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
        barraBusquedaCanciones = getView().findViewById(R.id.barraBusquedaCanciones);
        botonBusquedaCanciones = getView().findViewById(R.id.botonBusquedaCanciones);
        barraBusquedaAlbumes = getView().findViewById(R.id.barraBusquedaAlbumes);
        botonBusquedaAlbumes = getView().findViewById(R.id.botonBusquedaAlbumes);
        barraBusquedaArtistas = getView().findViewById(R.id.barraBusquedaArtistas);
        botonBusquedaArtistas = getView().findViewById(R.id.botonBusquedaArtistas);


        botonBusquedaCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = barraBusquedaCanciones.getText().toString();
                buscarCancion();
            }
        });

        botonBusquedaAlbumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = barraBusquedaAlbumes.getText().toString();
                buscarAlbum();
            }
        });

        botonBusquedaArtistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = barraBusquedaArtistas.getText().toString();
                buscarArtista();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busqueda, container, false);
    }

    private void buscarCancion() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/song/getByName?name=" + busqueda;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Cancion cancion = new Cancion();
                                cancion.setId(response.getJSONObject(i).getInt("id"));
                                cancion.setName(response.getJSONObject(i).getString("name"));
                                cancion.setFecha_subida(response.getJSONObject(i).getString("fecha_subida"));
                                cancion.setDuracion(response.getJSONObject(i).getInt("duracion"));
                                cancion.setAlbum(response.getJSONObject(i).getString("album"));
                                cancion.setArtistas(response.getJSONObject(i).getString("artistas"));
                                cancionesEncontradas.add(cancion);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoCancionesBusquedaFragment().newInstance(cancionesEncontradas)).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error busqueda cancion: " + error.toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoCancionesBusquedaFragment().newInstance(cancionesEncontradas)).commit();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void buscarAlbum(){
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/album/getByTitulo?titulo=" + busqueda;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Album albumBuscado = new Album();
                                Artista artista = new Artista();
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
                                albumesEncontrados.add(albumBuscado);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoAlbumesBusquedaFragment().newInstance(albumesEncontrados)).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error: " + error.toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoAlbumesBusquedaFragment().newInstance(albumesEncontrados)).commit();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void buscarArtista() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/artist/getByName?name=" + busqueda;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Artista obj;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                obj = gson.fromJson(response.getJSONObject(i).toString(), Artista.class);
                                artistasEncontrados.add(obj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoArtistaBusquedaFragment().newInstance(artistasEncontrados)).commit();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error busqueda cancion: " + error.toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoArtistaBusquedaFragment().newInstance(artistasEncontrados)).commit();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }
}
