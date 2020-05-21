package com.example.carolshaw.adapters;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carolshaw.PanelSocialFragment;
import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Amigo;

import java.util.ArrayList;

public class PanelSocialAdapter extends RecyclerView.Adapter<PanelSocialAdapter.Datos>
implements View.OnClickListener {

    ArrayList<Amigo> listAmigos;
    private OnItemClickListener mListener;
    PanelSocialFragment fragLocal;
    int position;
    Amigo amigo;

    public ImageView borrar;
    ImageView fotoPerf;

    public PanelSocialAdapter(ArrayList<Amigo> listAmigos, PanelSocialFragment frag) {
        this.listAmigos = listAmigos;
        this.fragLocal = frag;
    }

    @NonNull
    @Override
    public Datos onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_amigo,null,false);
        view.setOnClickListener(this);
        return new Datos(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Datos holder, int position) {
        holder.establecer(listAmigos.get(position));
        this.position = position;
    }

    @Override
    public void onClick(View v) {

    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        this.mListener = onClickListener;
    }


    public static class ResultViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        int position = -1;
        ImageView id;

        public ResultViewHolder (View view) {
            super(view);
            id = (ImageView) view.findViewById(R.id.btnBorrarAmigo);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {}
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }


    @Override
    public int getItemCount() {
            return listAmigos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class Datos extends RecyclerView.ViewHolder {

        TextView dato1;
        TextView dato2;
        TextView dato3;


        public Datos(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            dato1 = (TextView) itemView.findViewById(R.id.nickAm);
            dato2 = (TextView) itemView.findViewById(R.id.nomAm);
            dato3 = (TextView) itemView.findViewById(R.id.ultimaRepr);
            fotoPerf = (ImageView) itemView.findViewById(R.id.fotoPerfil);
            borrar = (ImageView) itemView.findViewById(R.id.btnBorrarAmigo);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            borrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                }
            });


        }

        public void establecer(Amigo amigo) {

            dato1.setText(amigo.getNick());
            dato2.setText(amigo.getNombre()+" "+ amigo.getApellidos());
            char idA = amigo.getAvatar().charAt(0);
            if(idA=='1') fotoPerf.setImageResource(R.drawable.perfil1);
            else if (idA=='2') fotoPerf.setImageResource(R.drawable.perfil2);
            else if (idA=='3') fotoPerf.setImageResource(R.drawable.perfil3);
            else if (idA=='4') fotoPerf.setImageResource(R.drawable.perfil4);
            else if (idA=='5') fotoPerf.setImageResource(R.drawable.perfil5);
            else if (idA=='6') fotoPerf.setImageResource(R.drawable.perfil6);

            if (!amigo.getUltimaCancion().equals("") && !amigo.getArtistaUltimaCancion().equals("")) {
                dato3.setText(amigo.getUltimaCancion()+" - "+ amigo.getArtistaUltimaCancion());
            }
            else {
                dato3.setText("Ultima canción -  Ultimo artista");
            }

        }
    }
}
