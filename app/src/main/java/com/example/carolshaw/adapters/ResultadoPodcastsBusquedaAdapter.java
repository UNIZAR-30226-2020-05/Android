package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Podcast;

import java.util.ArrayList;

public class ResultadoPodcastsBusquedaAdapter extends RecyclerView.Adapter<ResultadoPodcastsBusquedaAdapter.Datos>
        implements View.OnClickListener{

    ArrayList<Podcast> array;
    private View.OnClickListener listener;

    public ResultadoPodcastsBusquedaAdapter(ArrayList<Podcast> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public ResultadoPodcastsBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_podcast,null,false);
        view.setOnClickListener(this);
        return new ResultadoPodcastsBusquedaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoPodcastsBusquedaAdapter.Datos holder, int position) {
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
        TextView autor;
        TextView duracion;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tituloPodcast);
            autor = itemView.findViewById(R.id.autorPodcast);
            duracion = itemView.findViewById(R.id.duracionPodcast);
        }

        public void establecer(Podcast podcast) {
            nombre.setText(podcast.getName());
            autor.setText(podcast.getArtista());
            duracion.setText(podcast.getDuracionMMSS());
        }
    }
}
