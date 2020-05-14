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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumesArray = (ArrayList<Album>) getArguments().getSerializable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_albumes_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewAlbumes);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        ResultadoAlbumesBusquedaAdapter adapter = new ResultadoAlbumesBusquedaAdapter(albumesArray);
        recycler.setAdapter(adapter);
        return vista;
    }
}
