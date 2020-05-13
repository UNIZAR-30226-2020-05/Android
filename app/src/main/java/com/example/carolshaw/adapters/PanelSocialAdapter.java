package com.example.carolshaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carolshaw.R;
import com.example.carolshaw.objetos.Amigo;

import java.util.ArrayList;

public class PanelSocialAdapter extends RecyclerView.Adapter<PanelSocialAdapter.Datos>
implements View.OnClickListener {

    private View.OnClickListener listener;
    ArrayList<Amigo> listAmigos;

    public PanelSocialAdapter(ArrayList<Amigo> listAmigos) {
        this.listAmigos = listAmigos;
    }

    @NonNull
    @Override
    public Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_amigo,null,false);
        view.setOnClickListener(this);
        return new Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Datos holder, int position) {
        holder.establecer(listAmigos.get(position));
    }

    public void setOnClickListener(View.OnClickListener listen) {
        this.listener = listen;
    }

    @Override
    public int getItemCount() {
        if (listAmigos != null) {
            return listAmigos.size();
        }
        else return 0;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class Datos extends RecyclerView.ViewHolder {

        TextView dato1;
        TextView dato2;

        public Datos(@NonNull View itemView) {
            super(itemView);
            dato1 = (TextView) itemView.findViewById(R.id.nickAm);
            dato2 = (TextView) itemView.findViewById(R.id.nomAm);
        }

        public void establecer(Amigo amigo) {
            dato1.setText(amigo.getNick());
            dato2.setText(amigo.getNombre()+" "+ amigo.getApellidos());
        }
    }
}
