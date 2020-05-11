package com.example.carolshaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.objetos.Cancion;

import java.util.ArrayList;

public class CancionesAlbumAdapter extends RecyclerView.Adapter<CancionesAlbumAdapter.Datos> {

    ArrayList<Cancion> listaCanciones;

    public CancionesAlbumAdapter(ArrayList<Cancion> listaCanciones) {
        this.listaCanciones = listaCanciones;
    }

    @NonNull
    @Override
    public Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_cancion,null,false);
        return new CancionesAlbumAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Datos holder, int position) {
        holder.establecer(listaCanciones.get(position));
    }

    @Override
    public int getItemCount() {
        return listaCanciones.size();
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView tituloCancion;
        TextView albumCancion;
        TextView duracionCancion;

        public Datos(@NonNull View itemView) {
            super(itemView);
            tituloCancion = itemView.findViewById(R.id.tituloCancion);
            albumCancion = itemView.findViewById(R.id.albumCancion);
            duracionCancion = itemView.findViewById(R.id.duracionCancion);
        }

        public void establecer(Cancion cancion) {
            tituloCancion.setText(cancion.getName());
            albumCancion.setText(cancion.getAlbum());
            duracionCancion.setText(cancion.getDuracionMMSS());
        }
    }
}
