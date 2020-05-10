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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.UsuarioDto;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class PrincipalFragment extends Fragment {
    private ImageView album1;
    private ImageView album2;
    private ImageView album3;
    private ImageView album4;
    private ImageView podcast1;
    private ImageView podcast2;
    private ImageView podcast3;
    private ImageView podcast4;
    private String URL_API;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        iniciarImageviews();
        obtenerAlbumes();
        String url = "https://img.discogs.com/SODrOhivmrdNjpqakUNjAQFjdOM=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-1128943-1517158707-1062.png.jpg";
        ImageView album1 = getView().findViewById(R.id.album1);

        Picasso.get()
                .load(url)
                .resize(250, 250)
                .centerCrop()
                .into(album1);
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
                        Log.d("PrincipalFragment", "Exito al obtener albumes: " + response.toString());
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(2);
                            Log.d("PrincipalFragment", "id2: " + jsonObject1.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        album1 = getView().findViewById(R.id.album1);
        album2 = getView().findViewById(R.id.album2);
        album3 = getView().findViewById(R.id.album3);
        album4 = getView().findViewById(R.id.album4);
        podcast1 = getView().findViewById(R.id.podcast1);
        podcast2 = getView().findViewById(R.id.podcast2);
        podcast3 = getView().findViewById(R.id.podcast3);
        podcast4 = getView().findViewById(R.id.podcast4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }
}
