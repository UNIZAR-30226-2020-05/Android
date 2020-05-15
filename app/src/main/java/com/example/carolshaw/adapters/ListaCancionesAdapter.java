package com.example.carolshaw.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.ListaCancion;

import java.util.ArrayList;

public class ListaCancionesAdapter extends RecyclerView.Adapter<ListaCancionesAdapter.Datos>
        implements View.OnClickListener{

    private View.OnClickListener listener;
    ArrayList<ListaCancion> listasCanciones;

    public ListaCancionesAdapter(ArrayList<ListaCancion> listasCanciones) {
        this.listasCanciones = listasCanciones;
    }

    @NonNull
    @Override
    public ListaCancionesAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_lista_cancion,null,false);
        view.setOnClickListener(this);
        return new ListaCancionesAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaCancionesAdapter.Datos holder, int position) {
        holder.establecer(listasCanciones.get(position));
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
        if (listasCanciones != null) {
            return listasCanciones.size();
        }
        else return 0;
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView nombreLista;

        public Datos(@NonNull View itemView) {
            super(itemView);
            nombreLista = itemView.findViewById(R.id.nombreLista);
        }

        public void establecer(ListaCancion listaCancion) {
            nombreLista.setText(listaCancion.getNombre());
        }
    }
}
