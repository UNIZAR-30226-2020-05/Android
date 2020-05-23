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
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;

import java.io.Serializable;
import java.util.ArrayList;



public class ResultadoCancionesBusquedaFragment extends Fragment {
    private static final String ARG_PARAM1 = "canciones";
    private String URL_API;

    private static ArrayList<Cancion> cancionesArray;

    private RecyclerView recycler;
    ResultadoCancionesBusquedaAdapter adapter;
    private UsuarioDto usuarioLog;

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
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                opcionesClick(cancionesArray.get(recycler.getChildAdapterPosition(v)).getId(),
                        cancionesArray.get(recycler.getChildAdapterPosition(v)));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cancionesArray = (ArrayList<Cancion>) getArguments().getSerializable(ARG_PARAM1);
        }
        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_resultado_canciones_busqueda, container, false);
        recycler = vista.findViewById(R.id.recyclerViewListas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ResultadoCancionesBusquedaAdapter(cancionesArray);
        recycler.setAdapter(adapter);
        return vista;
    }

    private void opcionesClick(final int idCancion, final Cancion cancionReproductor){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");

        // Set up the buttons
        builder.setPositiveButton("Reproducir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainLogged.class);
                Bundle b = new Bundle();
                ArrayList<Cancion> cancionesReproductor = new ArrayList<Cancion>();
                cancionesReproductor.add(cancionReproductor); //Añade la cancion correspondiente
                b.putSerializable("canciones", cancionesReproductor);
                b.putInt("tipo",ReproductorFragment.TIPO_CANCION);
                b.putBoolean("nuevo",true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Añadir a lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                elegirLista(idCancion);
            }
        });
        builder.show();
    }

    private void elegirLista(int idCancion) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AnadirCancionesALista().newInstance(idCancion, AnadirCancionesALista.TIPO_CANCION)).commit();
    }


}
