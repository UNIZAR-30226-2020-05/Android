package com.example.carolshaw;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.adapters.PanelSocialBusqAdapter;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PanelSocialBusqFragment extends Fragment {

    private String URL_API;
    private Integer idUsuLoageado;
    private static ArrayList<UsuarioDto> usuarios;
    private RecyclerView recycler;
    private ArrayList<String> registroAmigos;

    public PanelSocialBusqFragment() {
        // Required empty public constructor
    }


    public static PanelSocialBusqFragment newInstance(Serializable usuarioLogeado) {
        PanelSocialBusqFragment fragment = new PanelSocialBusqFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cargarTodosUsuarios();
     ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);
        usuarios = new ArrayList<UsuarioDto>();
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos
            return;
        }
        idUsuLoageado = datosRecuperados.getInt("idUsu");
        registroAmigos = datosRecuperados.getStringArrayList("listaActual");

        Log.d("LLEGA",idUsuLoageado.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_anyadir_amigos, container, false);

        return vista;
    }

    public void cargarTodosUsuarios() {

        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API +"/user/findAll";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, peticion, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                UsuarioDto usu = new UsuarioDto();
                                usu.setId(response.getJSONObject(i).getInt("id"));
                                usu.setNick(response.getJSONObject(i).getString("nick"));
                                usu.setNombre(response.getJSONObject(i).getString("nombre"));
                                usu.setApellidos(response.getJSONObject(i).getString("apellidos"));
                                usu.setNombre_avatar(response.getJSONObject(i).getString("nombre_avatar"));

                                Log.d("zzz",usu.getNick());
                                usuarios.add(usu);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        activarVista();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("PanelSocialFragment", "error fetch " + error.toString());
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    public void activarVista() {
        recycler = getView().findViewById(R.id.recyclerAgregarAmigo);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recycler.setItemViewCacheSize(50);
        PanelSocialBusqAdapter adapter = new PanelSocialBusqAdapter(usuarios);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                if (registroAmigos.contains(usuarios.get(recycler.getChildAdapterPosition(v)).
                        getNick())) {
                    Toast.makeText(getContext(), usuarios.get(recycler.getChildAdapterPosition(v)).
                            getNick()+ " ya está en la lista de amigos", Toast.LENGTH_LONG).show();
                }
                else if(usuarios.get(recycler.getChildAdapterPosition(v)).getId()==idUsuLoageado) {
                    Toast.makeText(getContext(), "No puedes añadirte a ti mismo",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    registroAmigos.add(usuarios.get(recycler.getChildAdapterPosition(v)).
                            getNick());
                    anyadirAmigo(usuarios.get(recycler.getChildAdapterPosition(v)).getId());
                }
            }
        });
        recycler.setAdapter(adapter);
    }

    public void anyadirAmigo(final Integer id2) {

        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API +"/user/addAmigo/" + idUsuLoageado;
        JSONObject params = new JSONObject();
        try {
            params.put("id2", id2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, peticion, null ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Cuando llega la respuesta de objeto usuario
                        Toast.makeText(getContext(), "Añadido a la lista", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("PanelSocialFragment","error"); }
                }) {
            @Override
            public byte[] getBody() {
                return id2.toString().getBytes();
            }
        };


        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
}

