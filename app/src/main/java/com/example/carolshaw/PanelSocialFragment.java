package com.example.carolshaw;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Amigo;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.*;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class PanelSocialFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    ArrayList<Amigo> listAmigos;
    RecyclerView recycler;
    private String URL_API;

    private FloatingActionButton amigo;

    public PanelSocialFragment() {
        // Required empty public constructor
    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        amigo= getView().findViewById(R.id.btnAmigo);
        URL_API = getString(R.string.API);
        amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAmigos();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_social, container, false);
        listAmigos = new ArrayList<>();
        recycler = vista.findViewById(R.id.recyclerView2);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        listAmigos = new ArrayList<>();
        Amigo a = new Amigo();
        a.setNick("Nick objeto amigo");
        a.setNombre("Nombre"); a.setApellidos("Apellidos");
        listAmigos.add(a); listAmigos.add(a); listAmigos.add(a); listAmigos.add(a);
        PanelSocialAdapter adapter = new PanelSocialAdapter(listAmigos);
        recycler.setAdapter(adapter);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(R.id.fotoPerfil);
        return vista;
    }

    private void cargarAmigos() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        final UsuarioDto userLogeado = (UsuarioDto) getActivity().getApplicationContext();
        String peticion = URL_API +"/user/get?nick=" + userLogeado.getNick();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        UsuarioDto obj = gson.fromJson(response.toString(), UsuarioDto.class);
                        //Llega lista correcta.
                        listAmigos = obj.getAmigos();
                        userLogeado.setAmigos(obj.getAmigos());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

}
