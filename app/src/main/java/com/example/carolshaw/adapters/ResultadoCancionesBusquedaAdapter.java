package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Cancion;

import java.util.ArrayList;

public class ResultadoCancionesBusquedaAdapter extends RecyclerView.Adapter<ResultadoCancionesBusquedaAdapter.Datos>{

    ArrayList<Cancion> array;

    public ResultadoCancionesBusquedaAdapter(ArrayList<Cancion> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoCancionesBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_cancion,null,false);
        return new ResultadoCancionesBusquedaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoCancionesBusquedaAdapter.Datos holder, int position) {
        holder.establecer(array.get(position));
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView album;
        TextView duracion;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tituloCancion);
            album = itemView.findViewById(R.id.albumCancion);
            duracion = itemView.findViewById(R.id.duracionCancion);
        }

        public void establecer(Cancion cancion) {
            nombre.setText(cancion.getName());
            album.setText(cancion.getAlbum());
            duracion.setText(cancion.getDuracionMMSS());
        }
    }
}
