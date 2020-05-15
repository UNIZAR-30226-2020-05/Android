package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;

import java.util.ArrayList;

public class CancionesListaActivity extends AppCompatActivity {

    private ArrayList<Cancion> canciones = null;
    private String nombreLista;
    private TextView tituloVista;
    private RecyclerView recycler;
    private ResultadoCancionesBusquedaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones_lista);
        tituloVista = findViewById(R.id.tituloLista);
        recycler = findViewById(R.id.recyclerViewCanciones);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
            nombreLista = b.getString("nombre");
            tituloVista.setText(nombreLista);
            adapter = new ResultadoCancionesBusquedaAdapter(canciones);
            recycler.setLayoutManager(new LinearLayoutManager(CancionesListaActivity.this,
                    LinearLayoutManager.VERTICAL,false));
            recycler.setAdapter(adapter);
        }
    }
}
