package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carolshaw.adapters.ResultadoAlbumesBusquedaAdapter;
import com.example.carolshaw.objetos.Album;

import java.io.Serializable;
import java.util.ArrayList;



public class ResultadoAlbumesBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "albumes";
    private String URL_API;
    private ResultadoAlbumesBusquedaAdapter adapter;

    private static ArrayList<Album> albumesArray;

    private RecyclerView recycler;

    public ResultadoAlbumesBusquedaFragment() {
        // Required empty public constructor
    }


    public static ResultadoAlbumesBusquedaFragment newInstance(Serializable albumes) {
        ResultadoAlbumesBusquedaFragment fragment = new ResultadoAlbumesBusquedaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, albumes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                mostrarAlbum(albumesArray.get(recycler.getChildAdapterPosition(v)));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumesArray = (ArrayList<Album>) getArguments().getSerializable(ARG_PARAM1);
        }
        URL_API = getString(R.string.API);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_albumes_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewAlbumes);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ResultadoAlbumesBusquedaAdapter(albumesArray);
        recycler.setAdapter(adapter);
        return vista;
    }

    private void mostrarAlbum(Album album) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CancionesAlbumFragment().newInstance(album)).commit();
    }
}
