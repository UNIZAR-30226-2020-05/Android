package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carolshaw.adapters.ResultadoArtistasBusquedaAdapter;
import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Artista;
import com.example.carolshaw.objetos.Cancion;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadoArtistaBusquedaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadoArtistaBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "artistas";

    private static ArrayList<Artista> artistasArray;

    private RecyclerView recycler;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistasArray = (ArrayList<Artista>) getArguments().getSerializable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_artista_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewArtistas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        ResultadoArtistasBusquedaAdapter adapter = new ResultadoArtistasBusquedaAdapter(artistasArray);
        recycler.setAdapter(adapter);
        return vista;
    }
}
