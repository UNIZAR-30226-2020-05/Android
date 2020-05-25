package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ResultadoPodcastsBusquedaAdapter.OnItemClickListener mListener;
    int position;
    private boolean perteneceUsuario;

    public ResultadoPodcastsBusquedaAdapter(ArrayList<Podcast> array, boolean perteneceUsuario) {
        this.array = array;
        this.perteneceUsuario = perteneceUsuario;
    }

    @NonNull
    @Override
    public ResultadoPodcastsBusquedaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_podcast,null,false);
        view.setOnClickListener(this);
        return new ResultadoPodcastsBusquedaAdapter.Datos(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoPodcastsBusquedaAdapter.Datos holder, int position) {
        holder.establecer(array.get(position));
        this.position = position;
    }

    public void setOnClickListener(View.OnClickListener listen) {
        this.listener = listen;
    }

    public void setOnItemClickListener(ResultadoPodcastsBusquedaAdapter.OnItemClickListener onClickListener){
        this.mListener = onClickListener;
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        int position = -1;
        ImageView id;

        public ResultViewHolder (View view) {
            super(view);
            id = (ImageView) view.findViewById(R.id.btnBorrarCancion);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {}
    }

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public long getItemId(int position){ return  position;}

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
        ImageView btnBorrar;

        public Datos(@NonNull View itemView,  final ResultadoPodcastsBusquedaAdapter.OnItemClickListener listener) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tituloPodcast);
            autor = itemView.findViewById(R.id.autorPodcast);
            duracion = itemView.findViewById(R.id.duracionPodcast);
            btnBorrar = itemView.findViewById(R.id.btnBorrarPodcast);

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
        }

        public void establecer(Podcast podcast) {
            nombre.setText(podcast.getName());
            autor.setText(podcast.getArtista());
            duracion.setText(podcast.getDuracionMMSS());
            if (!perteneceUsuario) {
                btnBorrar.setVisibility(View.GONE);
            }
        }
    }
}
