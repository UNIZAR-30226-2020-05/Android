package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.carolshaw.objetos.Cancion;

import java.util.ArrayList;

public class CancionesListaActivity extends AppCompatActivity {

    private ArrayList<Cancion> canciones = null;
    private String nombreLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones_lista);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
            nombreLista = b.getString("nombre");
        }
        Log.d("ListaCanciones", "nombre: " + nombreLista);
    }
}
