package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Amigo;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class CancionesAlbumFragment extends Fragment {
    private static final String ALBUM = "albumRecibido";

    private Album album;
    private String URL_API;
    RecyclerView recycler;
    ImageView caratula;
    TextView titulo;
    TextView artistas;
    private ArrayList<Cancion> lisaCanciones;

    public CancionesAlbumFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
    }


    public static CancionesAlbumFragment newInstance(Album album) {
        CancionesAlbumFragment fragment = new CancionesAlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable(ALBUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_canciones_album, container, false);
        recycler = vista.findViewById(R.id.recyclerViewCanciones);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        caratula = vista.findViewById(R.id.caratulaAlbum);
        titulo = vista.findViewById(R.id.tituloAlbum);
        artistas = vista.findViewById(R.id.artistaAlbum);

        titulo.setText(album.getTitulo());
        artistas.setText(album.getArtista().getName());
        CancionesAlbumAdapter adapter = new CancionesAlbumAdapter(album.getCanciones());
        recycler.setAdapter(adapter);
        Picasso.get().load(album.getCaratula()).into(caratula);
        return vista;
    }
}
