package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Amigo;

import org.w3c.dom.Text;

public class PerfilAmigoAdapter extends RecyclerView.Adapter<PerfilAmigoAdapter.Datos> {

    Amigo amigo;
    int position;

    public PerfilAmigoAdapter(Amigo amigo) {
        this.amigo = amigo;
    }

    @NonNull
    @Override
    public PerfilAmigoAdapter.Datos onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_perfil_amigo,null,false);
        return new PerfilAmigoAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilAmigoAdapter.Datos holder, int position) {
        holder.establecer(amigo);
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class Datos extends RecyclerView.ViewHolder {

        ImageView fotoPerfil;
        TextView nickAmigo;
        TextView nombreAmigo;
        TextView ultimaReproduccion;

        public Datos(@NonNull final View itemView) {
            super(itemView);

            fotoPerfil = itemView.findViewById(R.id.fotoPerfil);
            nickAmigo = itemView.findViewById(R.id.nickAmigo);
            nombreAmigo = itemView.findViewById(R.id.nombreAmigo);
            ultimaReproduccion = itemView.findViewById(R.id.ultimaReproduccion);

        }

        public void establecer(Amigo amigo) {

            nickAmigo.setText(amigo.getNick());
            nombreAmigo.setText(amigo.getNombre()+" "+ amigo.getApellidos());
            char idA = amigo.getAvatar().charAt(0);
            if(idA=='1') fotoPerfil.setImageResource(R.drawable.perfil1);
            else if (idA=='2') fotoPerfil.setImageResource(R.drawable.perfil2);
            else if (idA=='3') fotoPerfil.setImageResource(R.drawable.perfil3);
            else if (idA=='4') fotoPerfil.setImageResource(R.drawable.perfil4);
            else if (idA=='5') fotoPerfil.setImageResource(R.drawable.perfil5);
            else if (idA=='6') fotoPerfil.setImageResource(R.drawable.perfil6);

            if (!amigo.getUltimaCancion().equals("") && !amigo.getArtistaUltimaCancion().equals("")) {
                ultimaReproduccion.setText(amigo.getUltimaCancion()+" - "+ amigo.getArtistaUltimaCancion());
            }
            else {
                ultimaReproduccion.setText("Ultima canción -  Ultimo artista");
            }

        }
    }
}
