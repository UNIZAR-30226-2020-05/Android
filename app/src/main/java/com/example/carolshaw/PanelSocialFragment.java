package com.example.carolshaw;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    public UsuarioDto userLogeado;
    RecyclerView recycler;
    private String URL_API;
    private FloatingActionButton amigo;
    private ImageView borrarAmigo;

    public PanelSocialFragment() {
    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        amigo= getView().findViewById(R.id.btnAmigo);
        URL_API = getString(R.string.API);
        amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarAbusquedaAmig();
            }
        });


        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        userLogeado = (UsuarioDto) getActivity().getApplicationContext();

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
                    public void onErrorResponse(VolleyError error) { Log.d("PanelSocialFragment",error.toString()); }
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
        return vista;
    }

    public void cargarLista(ArrayList<Amigo> lst) {
        final PanelSocialAdapter adapter = new PanelSocialAdapter(listAmigos, null);
        adapter.setOnItemClickListener(new PanelSocialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PerfilAmigoFragment().newInstance(listAmigos.get(position))).commit();
            }

            @Override
            public void onDeleteClick(int position) {
                //Que pasa cuando se le da al icono papelera

                try {
                    Toast.makeText(getContext(), listAmigos.get(position).getNick() +
                            " eliminado de la lista de amigos", Toast.LENGTH_LONG).show();
                    eliminarAmigo(listAmigos.get(position).getId());
                    listAmigos.remove(position);
                    adapter.notifyItemRemoved(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        recycler = getView().findViewById(R.id.recyclerView2);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(adapter);
        listAmigos.addAll(lst);
        Log.d("PanelSocialFragment", "tamaño lista: " + String.valueOf(listAmigos.size()));
    }

    public void cambiarAbusquedaAmig() {
        Bundle datos = new Bundle();
        datos.putInt("idUsu",userLogeado.getId());
        ArrayList<String> registroAmigos = new ArrayList<String>();
        for (int i=0; i<listAmigos.size(); i++) {
            registroAmigos.add(listAmigos.get(i).getNick());
        }
        datos.putStringArrayList("listaActual",registroAmigos);
        Fragment frag = new PanelSocialBusqFragment();
        frag.setArguments(datos);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        // Terminar transición.
        fragmentTransaction.commit();

    }

    public void eliminarAmigo(final Integer id) throws JSONException {

        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        UsuarioDto userLogeado = (UsuarioDto) getActivity().getApplicationContext();
        String peticion = URL_API +"/user/deleteAmigo/" + userLogeado.getId();


        JSONObject params = new JSONObject();
        try {
            params.put("id2", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HA borrado", "algo");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("PanelSocialFragment",error.toString()); }
                }) {
            @Override
            public byte[] getBody() {
                return id.toString().getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }
    
}
