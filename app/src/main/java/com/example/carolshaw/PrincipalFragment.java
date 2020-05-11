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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class PrincipalFragment extends Fragment {
    private ArrayList<ImageView> albumes;
    private ArrayList<String> albumesId = new ArrayList<String>();
    private ArrayList<ImageView> podcasts;
    private String URL_API;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        iniciarImageviews();
        obtenerAlbumes();

        albumes.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(0));
            }
        });
        albumes.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(1));
            }
        });
        albumes.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(2));
            }
        });
        albumes.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAlbum(albumesId.get(3));
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
                        String urlCaratula;
                        for(int i=0; i<response.length() && i<4; i++){
                            try {
                                urlCaratula = response.getJSONObject(i).getString("caratula");
                                Log.d("PrincipalFragment", "urlCaratula: " + urlCaratula);
                                Picasso.get()
                                        .load(urlCaratula)
                                        .resize(250, 250)
                                        .centerCrop()
                                        .into(albumes.get(i));
                                albumesId.add(response.getJSONObject(i).getString("id"));
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
        albumes = new ArrayList<ImageView>();
        albumes.add((ImageView) getView().findViewById(R.id.caratulaAlbum));
        albumes.add((ImageView) getView().findViewById(R.id.album2));
        albumes.add((ImageView) getView().findViewById(R.id.album3));
        albumes.add((ImageView) getView().findViewById(R.id.album4));
        podcasts = new ArrayList<ImageView>();
        podcasts.add((ImageView) getView().findViewById(R.id.podcast1));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast2));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast3));
        podcasts.add((ImageView) getView().findViewById(R.id.podcast4));
    }


    public void clickAlbum(String id){
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
