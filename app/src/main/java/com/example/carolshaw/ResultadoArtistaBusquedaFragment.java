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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ResultadoArtistasBusquedaAdapter;
import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;



public class ResultadoArtistaBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "artistas";

    private static ArrayList<Artista> artistasArray;
    private static ArrayList<Album> albumesArray;

    private String URL_API;

    private RecyclerView recycler;
    ResultadoArtistasBusquedaAdapter adapter;

    public ResultadoArtistaBusquedaFragment() {
        // Required empty public constructor
    }


    public static ResultadoArtistaBusquedaFragment newInstance(Serializable artistas) {
        ResultadoArtistaBusquedaFragment fragment = new ResultadoArtistaBusquedaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, artistas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAlbumesDeArtista(artistasArray.get(recycler.getChildAdapterPosition(v)).getId());
            }
        });

    }

    private void buscarAlbumesDeArtista(int indiceArtista) {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/album/getByArtist?id_artista=" + indiceArtista;

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        albumesArray = new ArrayList<Album>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Gson gson = new Gson();
                                Album obj = gson.fromJson(response.getJSONObject(i).toString(), Album.class);
                                albumesArray.add(obj);
                                Log.d("ResultadoArtista",String.valueOf(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoAlbumesBusquedaFragment().newInstance(albumesArray)).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ResultadoArtistaBusqueda", "error: " + error.toString());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ResultadoAlbumesBusquedaFragment().newInstance(albumesArray)).commit();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistasArray = (ArrayList<Artista>) getArguments().getSerializable(ARG_PARAM1);
        }
        URL_API = getString(R.string.API);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_artista_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewArtistas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ResultadoArtistasBusquedaAdapter(artistasArray);
        recycler.setAdapter(adapter);
        return vista;
    }

    private void mostrarAlbum(Album album) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ResultadoAlbumesBusquedaFragment().newInstance(albumesArray)).commit();
    }
}
