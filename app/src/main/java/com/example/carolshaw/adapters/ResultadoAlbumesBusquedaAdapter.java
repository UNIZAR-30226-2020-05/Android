package com.example.carolshaw.adapters;

import android.content.Context;
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
import com.example.carolshaw.objetos.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultadoAlbumesBusquedaAdapter extends RecyclerView.Adapter<ResultadoAlbumesBusquedaAdapter.Datos>
        implements View.OnClickListener{
    ArrayList<Album> array;
    private View.OnClickListener listener;
    private Context context;

    public ResultadoAlbumesBusquedaAdapter(ArrayList<Album> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoAlbumesBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_album,null,false);
        view.setOnClickListener(this);
        context = view.getContext();
        return new ResultadoAlbumesBusquedaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoAlbumesBusquedaAdapter.Datos holder, int position) {
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
            artista.setText(album.getArtista());
            Glide.with(context).load("https://3.18.169.143:8443" + album.getCaratula()).
                    apply(new RequestOptions().override(220, 220)).
                    into(imagen);
        }
    }
}
