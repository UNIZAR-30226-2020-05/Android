package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carolshaw.adapters.CancionesAlbumAdapter;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Cancion;
import com.squareup.picasso.Picasso;

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


    private ImageView anadirALista;
    private ImageView reproducirAlbum;

    public CancionesAlbumFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);

        anadirALista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AnadirCancionesALista().newInstance(album.getId(), AnadirCancionesALista.TIPO_ALBUM)).commit();
            }
        });

        reproducirAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                Toast.makeText(getContext(), "reproducir album entero", Toast.LENGTH_SHORT).show();
            }
        });
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
        anadirALista = vista.findViewById(R.id.btnAnadirAlbum);
        reproducirAlbum = vista.findViewById(R.id.btnPlayAlbum);
        recycler = vista.findViewById(R.id.recyclerViewListas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        caratula = vista.findViewById(R.id.caratulaAlbum);
        titulo = vista.findViewById(R.id.tituloAlbum);
        artistas = vista.findViewById(R.id.artistaAlbum);

        titulo.setText(album.getTitulo());
        artistas.setText(album.getArtista());
        CancionesAlbumAdapter adapter = new CancionesAlbumAdapter(album.getCanciones());
        recycler.setAdapter(adapter);
        Picasso.get().load(album.getCaratula()).into(caratula);
        return vista;
    }
}
