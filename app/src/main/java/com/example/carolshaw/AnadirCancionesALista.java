package com.example.carolshaw;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONObject;

/* Fragmento que dado un id de canción o album, lo añade a la lista seleccionada
 */
public class AnadirCancionesALista extends Fragment {

    private UsuarioDto usuarioLog;
    private static final String ID_CANCION = "id_cancion";
    private static final String TIPO = "tipo";
    private int idElemento;
    private int tipo;
    private String URL_API;
    private RecyclerView recycler;
    private ListaCancionesAdapter adapter;
    public static final int TIPO_CANCION = 0;
    public static final int TIPO_ALBUM = 1;

    public AnadirCancionesALista() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idElemento = getArguments().getInt(ID_CANCION);
            tipo = getArguments().getInt(TIPO);
        }
        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_lista_canciones, container, false);
        recycler = vista.findViewById(R.id.recyclerViewListas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ListaCancionesAdapter(usuarioLog.getLista_cancion());
        recycler.setAdapter(adapter);
        return vista;
    }

    public static AnadirCancionesALista newInstance(int idElemento, int tipo) {
        AnadirCancionesALista fragment = new AnadirCancionesALista();
        Bundle args = new Bundle();
        args.putInt(ID_CANCION, idElemento);
        args.putInt(TIPO, tipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                int p = recycler.getChildAdapterPosition(v);
                switch (tipo){
                    case TIPO_CANCION:
                        anadirCancion(idElemento,usuarioLog.getLista_cancion().get(p).getId(),p);
                        break;
                    case TIPO_ALBUM:
                        anadirAlbum(idElemento,usuarioLog.getLista_cancion().get(p).getId(),p);
                }
            }
        });
        /*getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ResultadoCancionesBusquedaFragment()).commit();*/
    }

    //TODO: añadir todo el album a la lista
    private void anadirAlbum(final int idAlbum, final int idLista, final int indiceLista) {
        Log.d("idAlbum","idAlbum: " + idAlbum + " lista: " + idLista);
        /*final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = URL_API + "/listaCancion/add/" + idAlbum;
        Log.d("idCancion", "idCancion: " + String.valueOf(idAlbum));
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaCancion obj = gson.fromJson(response.toString(), ListaCancion.class);
                        usuarioLog.deleteLista_cancion(idAlbum);
                        usuarioLog.addLista_cancion(obj);
                        informarCancionAnadida(obj.getNombre());
                        finalizarFragmento();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informarCancionRepetida();
                        finalizarFragmento();
                    }
                }) {
            @Override
            public byte[] getBody() {
                return String.valueOf(idCancion).getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);*/
    }

    private void anadirCancion(final int idCancion, final int idLista, final int indiceLista) {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = URL_API + "/listaCancion/add/" + idLista;
        Log.d("idCancion", "idCancion: " + String.valueOf(idCancion));
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaCancion obj = gson.fromJson(response.toString(), ListaCancion.class);
                        usuarioLog.deleteLista_cancion(indiceLista);
                        usuarioLog.addLista_cancion(obj);
                        informarCancionAnadida(obj.getNombre());
                        finalizarFragmento();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informarCancionRepetida();
                        finalizarFragmento();
                    }
                }) {
                @Override
                public byte[] getBody() {
                    return String.valueOf(idCancion).getBytes();
                }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void finalizarFragmento() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ResultadoCancionesBusquedaFragment()).commit();
    }

    /* informa mediante un TOAST de que la canción ha sido añadida a la lista
     */
    private void informarCancionAnadida(String nombreLista) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Canción añadida a " + nombreLista, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    /* informa mediante un TOAST de que la canción ya estaba en la lista y no se ha añadido
     */
    private void informarCancionRepetida() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "La canción ya está en la lista", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
