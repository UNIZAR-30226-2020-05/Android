package com.example.carolshaw;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.adapters.CancionesAlbumAdapter;
import com.example.carolshaw.objetos.Album;
import com.squareup.picasso.Picasso;


public class CancionesAlbumFragment extends Fragment {
    private static final String ALBUM = "albumRecibido";

    private Album album;
    private String URL_API;
    RecyclerView recycler;
    CancionesAlbumAdapter adapter;

    ImageView caratula;
    TextView titulo;
    TextView artistas;

    private ImageView anadirALista;
    private ImageView reproducirAlbum;

    public CancionesAlbumFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        Glide.with(getContext()).load(URL_API + album.getCaratula()).
                apply(new RequestOptions().override(150, 150)).
                into(caratula);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesClick(album.getCanciones().get(recycler.getChildAdapterPosition(v)).getId());
            }
        });

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
        caratula = vista.findViewById(R.id.album1);
        titulo = vista.findViewById(R.id.tituloAlbum);
        artistas = vista.findViewById(R.id.artistaAlbum);

        titulo.setText(album.getTitulo());
        artistas.setText(album.getArtista());
        adapter = new CancionesAlbumAdapter(album.getCanciones());
        recycler.setAdapter(adapter);
        return vista;
    }

    private void opcionesClick(final int idCancion){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");

        // Set up the buttons
        builder.setPositiveButton("Reproducir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Falta reproducir cancion", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Añadir a lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                elegirLista(idCancion);
                //anadirCancion(idCancion);
            }
        });
        builder.show();
    }

    private void elegirLista(int idCancion) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AnadirCancionesALista().newInstance(idCancion, AnadirCancionesALista.TIPO_CANCION)).commit();
    }}
