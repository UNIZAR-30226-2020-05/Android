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
import com.example.carolshaw.objetos.UsuarioDto;

import java.util.ArrayList;
import java.util.Random;

public class PanelSocialBusqAdapter extends RecyclerView.Adapter<PanelSocialBusqAdapter.Datos>
implements View.OnClickListener {

    private View.OnClickListener listener;
   // private ImageView btnBorrarAmigo;
    ArrayList<UsuarioDto> listTodos;

    TextView dat1;
    TextView dat2;
    ImageView fotoPerfil;

    public PanelSocialBusqAdapter(ArrayList<UsuarioDto> lista) {
        this.listTodos = lista;
    }

    @NonNull
    @Override
    public PanelSocialBusqAdapter.Datos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_busq_amigo,null,false);
        view.setOnClickListener(this);
        //btnBorrarAmigo.findViewById(R.id.btnBorrarAmigo);
        //btnBorrarAmigo.setOnClickListener(listener);
        return new PanelSocialBusqAdapter.Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelSocialBusqAdapter.Datos holder, int position) {
        //if (listTodos.size()>0) {
            holder.establecer(listTodos.get(position));
        //}
    }

    public void setOnClickListener(View.OnClickListener listen) {
        this.listener = listen;
    }

    @Override
    public int getItemCount() {
        if (listTodos != null) {
            return listTodos.size();
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

        public Datos(@NonNull View itemView) {
            super(itemView);
            dat1 = itemView.findViewById(R.id.agregNick);
            dat2 = itemView.findViewById(R.id.agregarNom);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.imgPerfilAmigo);
        }

        public void establecer(UsuarioDto usu) {
            if (usu.getNick() == null) {
                dat1.setText("NA");
                dat2.setText("DE NA");
            }
           else {
                dat1.setText(usu.getNick());
                dat2.setText((usu.getNombre()+ " "+ usu.getApellidos()));
                /*
                if ((usu.getNombre_avatar().equals("1"))) { fotoPerfil.setImageResource(R.drawable.perfil1); }
                else if ((usu.getNombre_avatar().equals("2"))) { fotoPerfil.setImageResource(R.drawable.perfil2); }
                else if ((usu.getNombre_avatar().equals("3"))) { fotoPerfil.setImageResource(R.drawable.perfil3); }
                else if ((usu.getNombre_avatar().equals("4"))) { fotoPerfil.setImageResource(R.drawable.perfil4); }
                else if ((usu.getNombre_avatar().equals("5"))) { fotoPerfil.setImageResource(R.drawable.perfil5); }
                else if ((usu.getNombre_avatar().equals("6"))) { fotoPerfil.setImageResource(R.drawable.perfil6); }
                */

            }

        }
    }
}
