package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Cancion;

import java.util.ArrayList;

public class CancionesListaAdapter extends RecyclerView.Adapter<CancionesListaAdapter.Datos>
        implements View.OnClickListener{

    ArrayList<Cancion> array;
    private View.OnClickListener listener;

    public CancionesListaAdapter(ArrayList<Cancion> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public CancionesListaAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_cancion,null,false);
        view.setOnClickListener(this);
        return new CancionesListaAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancionesListaAdapter.Datos holder, int position) {
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
        TextView album;
        TextView duracion;
        ImageView btnBorrar;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tituloCancion);
            album = itemView.findViewById(R.id.albumCancion);
            duracion = itemView.findViewById(R.id.duracionCancion);
            btnBorrar = itemView.findViewById(R.id.btnBorrarCancion);
        }

        public void establecer(Cancion cancion) {
            nombre.setText(cancion.getName());
            album.setText(cancion.getAlbum());
            duracion.setText(cancion.getDuracionMMSS());
        }
    }
}
