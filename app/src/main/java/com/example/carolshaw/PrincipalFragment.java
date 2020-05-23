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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Podcast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;


public class PrincipalFragment extends Fragment {
    private ArrayList<ImageView> imagesAlbum = new ArrayList<ImageView>();;
    private ArrayList<Album> albumes = new ArrayList<Album>();
    private ArrayList<ImageView> imagesPodcasts = new ArrayList<ImageView>();;
    private ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
    private ArrayList<TextView> tituloPodcast = new ArrayList<TextView>();
    private String URL_API;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        iniciarViews();
        obtenerAlbumes();
        obtenerPodcasts();

        imagesAlbum.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumes.get(0));
            }
        });
        imagesAlbum.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumes.get(1));
            }
        });
        imagesAlbum.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumes.get(2));
            }
        });
        imagesAlbum.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumes.get(3));
            }
        });

        imagesPodcasts.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPodcast(podcasts.get(0));
            }
        });
        imagesPodcasts.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPodcast(podcasts.get(1));
            }
        });
        imagesPodcasts.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPodcast(podcasts.get(2));
            }
        });
        imagesPodcasts.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPodcast(podcasts.get(3));
            }
        });
    }

    /* Obtiene la información de los albumes a mostrar
     */
    private void obtenerAlbumes() {
        HttpsTrustManager.allowAllSSL();
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlGet = URL_API + "/album/getByTitulo?titulo=";
        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray cancionesArrayJSON;
                        Log.d("principalFragment",response.toString());
                        for(int i=0; i<response.length(); i++){
                            try {
                                Gson gson = new Gson();
                                Album obj = gson.fromJson(response.getJSONObject(i).toString(), Album.class);
                                albumes.add(obj);
                                mostrarAlbumes();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error al obtener los álbumes");
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void mostrarAlbumes() {
        if(isAdded()) {
            for (int i = 0; i < 4 && i < albumes.size(); i++) {
                Glide.with(getContext()).load(URL_API + albumes.get(i).getCaratula()).
                        apply(new RequestOptions().override(220, 220)).
                        into(imagesAlbum.get(i));
            }
        }
    }

    /* Obtiene la información de los podcasts a mostrar
     */
    private void obtenerPodcasts() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/podcast/getByName?name=";

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Podcast obj;
                        try {
                            for (int i = 0; i < 4; i++) {
                                obj = gson.fromJson(response.getJSONObject(i).toString(), Podcast.class);
                                podcasts.add(obj);
                                tituloPodcast.get(i).setText(obj.getName());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BusquedaFragment", "error busqueda cancion: " + error.toString());
                        informar("Error al obtener los podcasts");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    /* Inicia las variables de Imageviews
     */
    private void iniciarViews() {
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album1));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album2));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album3));
        imagesAlbum.add((ImageView) getView().findViewById(R.id.album4));

        imagesPodcasts.add((ImageView) getView().findViewById(R.id.podcast1));
        imagesPodcasts.add((ImageView) getView().findViewById(R.id.podcast2));
        imagesPodcasts.add((ImageView) getView().findViewById(R.id.podcast3));
        imagesPodcasts.add((ImageView) getView().findViewById(R.id.podcast4));

        for (int i = 0; i < imagesPodcasts.size(); i++) {
            imagesPodcasts.get(i).setImageResource(R.drawable.podcast1);
        }
        tituloPodcast.add((TextView) getView().findViewById(R.id.tituloPodcast1));
        tituloPodcast.add((TextView) getView().findViewById(R.id.tituloPodcast2));
        tituloPodcast.add((TextView) getView().findViewById(R.id.tituloPodcast3));
        tituloPodcast.add((TextView) getView().findViewById(R.id.tituloPodcast4));
    }


    public void clickAlbum(Album album){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CancionesAlbumFragment().newInstance(album)).commit();
    }

    public void clickPodcast(Podcast podcast){
        Intent intent = new Intent(getContext(), MainLogged.class);
        Bundle b = new Bundle();
        ArrayList<Podcast> podcastsReproductor = new ArrayList<Podcast>();
        podcastsReproductor.add(podcast); //Añade la cancion correspondiente
        b.putSerializable("podcasts", podcastsReproductor);
        b.putInt("tipo",ReproductorFragment.TIPO_PODCAST);
        b.putBoolean("nuevo",true);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
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
