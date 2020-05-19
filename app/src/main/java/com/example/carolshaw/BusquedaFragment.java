package com.example.carolshaw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.Podcast;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class BusquedaFragment extends Fragment {

    private EditText barraBusquedaCanciones;
    private Button botonBusquedaCanciones;
    private EditText barraBusquedaAlbumes;
    private Button botonBusquedaAlbumes;
    private EditText barraBusquedaArtistas;
    private Button botonBusquedaArtistas;
    private EditText barraBusquedaPodcasts;
    private Button botonBusquedaPodcasts;
    private EditText barraBusquedaListas;
    private Button botonBusquedaListas;

    private UsuarioDto usuarioLog;
    private String URL_API;
    private String busqueda;
    private ArrayList<Album> albumesEncontrados = new ArrayList<Album>();
    private ArrayList<Cancion> cancionesEncontradas = new ArrayList<Cancion>();
    private ArrayList<Artista> artistasEncontrados = new ArrayList<Artista>();
    private ArrayList<Podcast> podcastsEncontrados = new ArrayList<Podcast>();

    public BusquedaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();
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
        barraBusquedaPodcasts = getView().findViewById(R.id.barraBusquedaPodcasts);
        botonBusquedaPodcasts = getView().findViewById(R.id.botonBusquedaPodcasts);
        barraBusquedaListas = getView().findViewById(R.id.barraBusquedaListas);
        botonBusquedaListas = getView().findViewById(R.id.botonBusquedaListas);


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

        botonBusquedaPodcasts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = barraBusquedaPodcasts.getText().toString();
                buscarPodcasts();
            }
        });

        botonBusquedaListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = barraBusquedaListas.getText().toString();
                buscarListaCancion();
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
                                Gson gson = new Gson();
                                Cancion obj = gson.fromJson(response.getJSONObject(i).toString(), Cancion.class);
                                cancionesEncontradas.add(obj);
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
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        //        new ResultadoCancionesBusquedaFragment().newInstance(cancionesEncontradas)).commit();
                        informar("Canción no encontrada");
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
                                Gson gson = new Gson();
                                Album obj = gson.fromJson(response.getJSONObject(i).toString(), Album.class);
                                albumesEncontrados.add(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(albumesEncontrados.size() > 0){
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ResultadoAlbumesBusquedaFragment().newInstance(albumesEncontrados)).commit();
                        } else {
                            informar("Album no encontrado");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error: " + error.toString());
                        informar("Error desconocido");
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
                        if (artistasEncontrados.size() > 0) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ResultadoArtistaBusquedaFragment().newInstance(artistasEncontrados)).commit();
                        } else {
                            informar("Artista no encontrado");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error busqueda cancion: " + error.toString());
                        informar("Error desconocido");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void buscarPodcasts() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/podcast/getByName?name=" + busqueda;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Podcast obj;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                obj = gson.fromJson(response.getJSONObject(i).toString(), Podcast.class);
                                podcastsEncontrados.add(obj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoPodcastsBusquedaFragment().newInstance(podcastsEncontrados)).commit();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error busqueda cancion: " + error.toString());
                        informar("Podcast no encontrado");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    // En primer lugar busca una lista de canción, si falla, prueba a buscar una lista de podcasts
    private void buscarListaCancion() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/listaCancion/get?id=" + busqueda;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaCancion obj;
                        obj = gson.fromJson(response.toString(), ListaCancion.class);

                        Intent intent = new Intent(getContext(), CancionesListaActivity.class);
                        Bundle b = new Bundle();
                        b.putString("nombre", obj.getNombre());
                        b.putInt("idLista", obj.getId());
                        b.putSerializable("canciones", obj.getCanciones());

                        intent.putExtras(b);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buscarListaPodcast();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void buscarListaPodcast() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/listaPodcast/get?id=" + busqueda;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaPodcast obj;
                        obj = gson.fromJson(response.toString(), ListaPodcast.class);

                        Intent intent = new Intent(getContext(), PodcastListaActivity.class);
                        Bundle b = new Bundle();
                        b.putString("nombre", obj.getNombre());
                        b.putInt("idLista", obj.getId());
                        b.putSerializable("podcasts", obj.getPodcasts());

                        intent.putExtras(b);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Lista no encontrada");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    /* informa mediante un TOAST
     */
    private void informar(String mensaje) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
