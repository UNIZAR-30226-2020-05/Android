package com.example.carolshaw;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.PanelSocialAdapter;
import com.example.carolshaw.objetos.Amigo;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PanelSocialFragment extends Fragment {



    public ArrayList<Amigo> listAmigos = new ArrayList<>();
    RecyclerView recycler;
    private String URL_API= "http://3.22.247.114:8080";

    private FloatingActionButton amigo;
    private ImageView borrarAmigo;

    public PanelSocialFragment() {
        // Required empty public constructor
    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        amigo= getView().findViewById(R.id.btnAmigo);
        //borrarAmigo = recycler.findViewById(R.id.btnBorrarAmigo);
       /* borrarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("ASD","WEEEEE");
                    eliminarAmigo(listAmigos.get(0).getId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/
        amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargarAmigos();
            }
        });


        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        UsuarioDto userLogeado = (UsuarioDto) getActivity().getApplicationContext();
        String peticion = URL_API +"/user/get?nick=" + userLogeado.getNick();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Cuando llega la respuesta de objeto usuario
                        Gson gson = new Gson();
                        UsuarioDto obj = gson.fromJson(response.toString(), UsuarioDto.class);

                        //Deserializa

                        cargarLista(obj.getAmigos());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("PanelSocialFragment","error"); }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_social, container, false);
       // listAmigos = new ArrayList<Amigo>();
      //  a.setNick("Nick objeto amigo");
      //  a.setNombre("Nombre"); a.setApellidos("Apellidos");
        //Log.d("ASDF", String.valueOf(listAmigos.size())+listAmigos.get(0).getNick()+"---"+listAmigos.get(0).getNombre());

        Log.d("AAAAAAA", String.valueOf(listAmigos.size()));

        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(R.id.fotoPerfil);
        return vista;
    }

    private void cargarLista(ArrayList<Amigo> lst) {
        PanelSocialAdapter adapter = new PanelSocialAdapter(listAmigos);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
               Toast.makeText(getContext(),"Tratar amigo elegido: " +
                       listAmigos.get(recycler.getChildAdapterPosition(v)).getNick(),
                       Toast.LENGTH_LONG).show();
            }
        });
        recycler = getView().findViewById(R.id.recyclerView2);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(adapter);
        listAmigos.addAll(lst);
        Log.d("PanelSocialFragment", "tamaño lista: " + String.valueOf(listAmigos.size()));
    }


    private void eliminarAmigo(Integer id) throws JSONException {

        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        UsuarioDto userLogeado = (UsuarioDto) getActivity().getApplicationContext();
        String peticion = URL_API +"'/user/deleteAmigo/" + userLogeado.getId();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", id);



        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, peticion, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.d("HA borrado", "algo");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("PanelSocialFragment","error"); }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
    
}
