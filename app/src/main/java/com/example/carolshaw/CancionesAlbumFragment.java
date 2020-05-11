package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Amigo;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;


public class CancionesAlbumFragment extends Fragment {
    private static final String ID_ALBUM = "idAlbumRecibido";
    private static final String TITULO_ALBUM = "tituloAlbumRecibido";

    private String idAlbum;
    private String tituloAlbum;
    private String URL_API;
    RecyclerView recycler;
    private ArrayList<Cancion> lisaCanciones;

    public CancionesAlbumFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        cargarCanciones();
    }

    private void cargarCanciones() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlGet = URL_API +"/album/get";

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Cancion obj = gson.fromJson(response.toString(), Cancion.class);

                        Log.d("CancionesAlbum","response: " + response.toString());
                        //Llega lista correcta.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CancionesAlbum","errore response: " + error.toString());
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    public static CancionesAlbumFragment newInstance(String idAlbum, String tituloAlbum) {
        CancionesAlbumFragment fragment = new CancionesAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ID_ALBUM, idAlbum);
        args.putString(TITULO_ALBUM, tituloAlbum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAlbum = getArguments().getString(ID_ALBUM);
            tituloAlbum = getArguments().getString(TITULO_ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_canciones_album, container, false);
        recycler = vista.findViewById(R.id.recyclerViewCanciones);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        /*CancionesAlbumAdapter adapter = new CancionesAlbumAdapter(list);
        recycler.setAdapter(adapter);*/
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(R.id.fotoPerfil);
        return vista;
    }
}
