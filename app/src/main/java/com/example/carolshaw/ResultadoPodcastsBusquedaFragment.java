package com.example.carolshaw;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.adapters.ResultadoPodcastsBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.Podcast;
import com.example.carolshaw.objetos.UsuarioDto;

import java.io.Serializable;
import java.util.ArrayList;



public class ResultadoPodcastsBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "Podcasts";
    private String URL_API;

    private static ArrayList<Podcast> PodcastsArray;

    private RecyclerView recycler;
    ResultadoPodcastsBusquedaAdapter adapter;
    private UsuarioDto usuarioLog;

    public ResultadoPodcastsBusquedaFragment() {
        // Required empty public constructor
    }


    public static ResultadoPodcastsBusquedaFragment newInstance(Serializable Podcasts) {
        ResultadoPodcastsBusquedaFragment fragment = new ResultadoPodcastsBusquedaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, Podcasts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                opcionesClick(PodcastsArray.get(recycler.getChildAdapterPosition(v)).getId(),
                        PodcastsArray.get(recycler.getChildAdapterPosition(v)));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PodcastsArray = (ArrayList<Podcast>) getArguments().getSerializable(ARG_PARAM1);
        }
        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_podcasts_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewPodcasts);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ResultadoPodcastsBusquedaAdapter(PodcastsArray);
        recycler.setAdapter(adapter);
        return vista;
    }

    private void opcionesClick(final int idPodcast, final Podcast podcastReproductor){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");

        // Set up the buttons
        builder.setPositiveButton("Reproducir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainLogged.class);
                Bundle b = new Bundle();
                ArrayList<Podcast> podcastsReproductor = new ArrayList<Podcast>();
                podcastsReproductor.add(podcastReproductor); //Añade la cancion correspondiente
                b.putSerializable("podcasts", podcastsReproductor);
                b.putInt("tipo",ReproductorFragment.TIPO_PODCAST);
                b.putBoolean("nuevo",true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Añadir a lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                elegirLista(idPodcast);
            }
        });
        builder.show();
    }

    private void elegirLista(int idPodcast) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AnadirPodcastALista().newInstance(idPodcast)).commit();
    }


}
