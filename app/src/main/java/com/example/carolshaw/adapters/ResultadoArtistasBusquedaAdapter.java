package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Artista;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultadoArtistasBusquedaAdapter extends RecyclerView.Adapter<ResultadoArtistasBusquedaAdapter.Datos>
        implements View.OnClickListener{
    ArrayList<Artista> array;
    private View.OnClickListener listener;

    public ResultadoArtistasBusquedaAdapter(ArrayList<Artista> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoArtistasBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_artista,null,false);
        view.setOnClickListener(this);
        return new ResultadoArtistasBusquedaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoArtistasBusquedaAdapter.Datos holder, int position) {
        holder.establecer(array.get(position));
    }

    public void setOnClickListener(View.OnClickListener listen) {
        this.listener = listen;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView nombre;
        ImageView imagen;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.artista);
            imagen = itemView.findViewById(R.id.imagenArtista);
        }

        public void establecer(Artista artista) {
            nombre.setText(artista.getName());
            Picasso.get().load(artista.getImage_path()).into(imagen);
        }
    }
}
