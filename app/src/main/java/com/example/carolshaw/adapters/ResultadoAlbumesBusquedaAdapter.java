package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultadoAlbumesBusquedaAdapter extends RecyclerView.Adapter<ResultadoAlbumesBusquedaAdapter.Datos>{
    ArrayList<Album> array;

    public ResultadoAlbumesBusquedaAdapter(ArrayList<Album> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoAlbumesBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_album,null,false);
        return new ResultadoAlbumesBusquedaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoAlbumesBusquedaAdapter.Datos holder, int position) {
        holder.establecer(array.get(position));
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView artista;
        ImageView imagen;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tituloAlbum);
            artista = itemView.findViewById(R.id.artistaAlbum);
            imagen = itemView.findViewById(R.id.imagenAlbum);
        }

        public void establecer(Album album) {
            nombre.setText(album.getTitulo());
            artista.setText(album.getArtista().getName());
            Picasso.get().load(album.getCaratula()).into(imagen);
        }
    }
}
