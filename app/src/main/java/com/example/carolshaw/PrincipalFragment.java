package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PrincipalFragment extends Fragment {
    private ArrayList<ImageView> imagesAlbum;
    private ArrayList<String> albumesId = new ArrayList<String>();
    private ArrayList<String> albumesTitulo = new ArrayList<String>();
    private ArrayList<Album> albumes = new ArrayList<Album>();
    private ArrayList<ImageView> podcasts;
    private String URL_API;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        iniciarImageviews();
        obtenerAlbumes();

        imagesAlbum.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(0),albumesTitulo.get(0));
            }
        });
        imagesAlbum.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(1),albumesTitulo.get(1));
            }
        });
        imagesAlbum.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(2),albumesTitulo.get(2));
            }
        });
        imagesAlbum.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(3),albumesTitulo.get(3));
            }
        });

    }

    /* Obtiene la información de los albumes a mostrar
     */
    private void obtenerAlbumes() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlGet = URL_API + "/album/getByTitulo?titulo=";
        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray cancionesArrayJSON;
                        for(int i=0; i<response.length() && i<4; i++){
                            try {
                                Album album = new Album();
                                ArrayList<Cancion> canciones = new ArrayList<Cancion>();
                                //Meter toda la informacion de un album en la lista
                                album.setId(Integer.parseInt(response.getJSONObject(i).getString("id")));
                                album.setTitulo(response.getJSONObject(i).getString("titulo"));
                                album.setCaratula(response.getJSONObject(i).getString("caratula"));
                                Artista artista = new Artista();
                                artista.setName(response.getJSONObject(i).getString("artista"));
                                album.setArtista(artista);

                                Cancion cancion = new Cancion();
                                cancionesArrayJSON = response.getJSONObject(i).getJSONArray("canciones"); //Obtiene las canciones del album
                                for(int j=0; j<cancionesArrayJSON.length(); j++){
                                    cancion.setId(cancionesArrayJSON.getJSONObject(j).getInt("id"));
                                    cancion.setName(cancionesArrayJSON.getJSONObject(j).getString("name"));
                                    cancion.setFecha_subida(cancionesArrayJSON.getJSONObject(j).getString("fecha_subida"));
                                    cancion.setDuracion(cancionesArrayJSON.getJSONObject(j).getInt("duracion"));
                                    cancion.setAlbum(cancionesArrayJSON.getJSONObject(j).getString("album"));
                                    cancion.setArtistas(cancionesArrayJSON.getJSONObject(j).getString("artistas"));
                                }
                                canciones.add(cancion);
                                album.setCanciones(canciones);
                                Picasso.get()
                                        .load(album.getCaratula())
                                        .resize(250, 250)
                                        .centerCrop()
                                        .into(imagesAlbum.get(i));
                                albumesId.add(String.valueOf(album.getId()));
                                albumes.add(album);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PrincipalFragment", "Error al obtener albumes: " + error.toString());

                    }
                });

        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    /* Inicia las variables de Imageviews
     */
    private void iniciarImageviews() {
        imagesAlbum = new ArrayList<ImageView>();
        imagesAlbum.add((ImageView) getView().findViewById(R.id.caratulaAlbum));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album2));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album3));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album4));
        podcasts = new ArrayList<ImageView>();
        podcasts.add((ImageView) getView().findViewById(R.id.podcast1));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast2));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast3));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast4));
    }


    public void clickAlbum(String id, String titulo){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CancionesAlbumFragment().newInstance(id,titulo)).commit();
    }

    public void clickPodcast(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }
}
