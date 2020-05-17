package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.ListaPodcast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListaPodcastsAdapter extends RecyclerView.Adapter<ListaPodcastsAdapter.Datos>
        implements View.OnClickListener{

    private View.OnClickListener listener;
    ArrayList<ListaPodcast> listasPodcasts;

    public ListaPodcastsAdapter(ArrayList<ListaPodcast> listasPodcasts) {
        this.listasPodcasts = listasPodcasts;
    }

    @NonNull
    @Override
    public ListaPodcastsAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_lista_cancion,null,false);
        view.setOnClickListener(this);
        return new ListaPodcastsAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPodcastsAdapter.Datos holder, int position) {
        holder.establecer(listasPodcasts.get(position));
    }

    public void setOnClickListener(View.OnClickListener listen) {
        this.listener = listen;
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @Override
    public int getItemCount() {
        if (listasPodcasts != null) {
            return listasPodcasts.size();
        }
        else return 0;
    }


    public class Datos extends RecyclerView.ViewHolder {

        TextView tituloPodcast;

        public Datos(@NonNull View itemView) {
            super(itemView);
            tituloPodcast = itemView.findViewById(R.id.nombreLista);
        }

        public void establecer(ListaPodcast listasPodcasts) {
            tituloPodcast.setText(listasPodcasts.getNombre());
        }


    }
}
