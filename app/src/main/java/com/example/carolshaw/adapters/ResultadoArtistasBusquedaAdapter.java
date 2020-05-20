package com.example.carolshaw.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Artista;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultadoArtistasBusquedaAdapter extends RecyclerView.Adapter<ResultadoArtistasBusquedaAdapter.Datos>
        implements View.OnClickListener{
    ArrayList<Artista> array;
    private View.OnClickListener listener;
    private Context context;

    public ResultadoArtistasBusquedaAdapter(ArrayList<Artista> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoArtistasBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_artista,null,false);
        view.setOnClickListener(this);
        context = view.getContext();
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
            Glide.with(context).load("https://3.18.169.143:8443" + artista.getImage_path()).
                    apply(new RequestOptions().override(220, 220)).
                    into(imagen);
        }
    }
}
