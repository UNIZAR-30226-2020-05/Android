package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;

import java.io.Serializable;
import java.util.ArrayList;



public class ResultadoCancionesBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "canciones";

    private static ArrayList<Cancion> cancionesArray;

    private RecyclerView recycler;

    public ResultadoCancionesBusquedaFragment() {
        // Required empty public constructor
    }


    public static ResultadoCancionesBusquedaFragment newInstance(Serializable canciones) {
        ResultadoCancionesBusquedaFragment fragment = new ResultadoCancionesBusquedaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, canciones);
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
            cancionesArray = (ArrayList<Cancion>) getArguments().getSerializable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_canciones_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewListas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        ResultadoCancionesBusquedaAdapter adapter = new ResultadoCancionesBusquedaAdapter(cancionesArray);
        recycler.setAdapter(adapter);
        return vista;
    }
}
