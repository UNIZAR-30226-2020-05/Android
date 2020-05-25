package com.example.carolshaw;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.adapters.ListaPodcastsAdapter;
import com.example.carolshaw.adapters.PerfilAmigoAdapter;
import com.example.carolshaw.objetos.Amigo;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;


public class PerfilAmigoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Amigo amigo;
    private String URL_API;

    private RecyclerView recyclerInfoAmigo;
    private RecyclerView recyclerListasCancionesAmigo;
    private RecyclerView recyclerListasPodcastsAmigo;
    private ImageButton btnCancionesPodcast;
    private final int tipoCancion = ReproductorFragment.TIPO_CANCION;
    private final int tipoPodcast = ReproductorFragment.TIPO_PODCAST;
    private int tipo;

    private ArrayList<ListaCancion> listasCanciones;
    private ArrayList<ListaPodcast> listasPodcasts;
    private ListaCancionesAdapter adapterListasCanciones;
    private ListaPodcastsAdapter adapterListasPodcasts;
    UsuarioDto amigoDto;

    public PerfilAmigoFragment() {
        // Required empty public constructor
    }

    public static PerfilAmigoFragment newInstance(Amigo param1) {
        PerfilAmigoFragment fragment = new PerfilAmigoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amigo = (Amigo) getArguments().getSerializable(ARG_PARAM1);
        }
        URL_API = getString(R.string.API);
        tipo = tipoCancion; //Muestra primero las listas de canciones
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PerfilAmigoAdapter adapterInfo = new PerfilAmigoAdapter(amigo);
        recyclerInfoAmigo.setAdapter(adapterInfo);
        descargarAmigo();

        btnCancionesPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipo == tipoCancion) {
                    btnCancionesPodcast.setImageResource(R.drawable.nota_musica);
                    recyclerListasCancionesAmigo.setVisibility(View.GONE);
                    recyclerListasPodcastsAmigo.setVisibility(View.VISIBLE);
                    tipo = tipoPodcast;
                } else if (tipo == tipoPodcast) {
                    btnCancionesPodcast.setImageResource(R.drawable.auriculares_podcast);
                    recyclerListasPodcastsAmigo.setVisibility(View.GONE);
                    recyclerListasCancionesAmigo.setVisibility(View.VISIBLE);
                    tipo = tipoCancion;
                }
                cargarListas();
            }
        });
    }

    private void descargarAmigo() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API +"/user/get?nick=" + amigo.getNick();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Cuando llega la respuesta de objeto usuario
                        Gson gson = new Gson();
                        amigoDto = gson.fromJson(response.toString(), UsuarioDto.class);
                        listasCanciones = amigoDto.getLista_cancion();
                        listasPodcasts = amigoDto.getLista_podcast();
                        cargarListas();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("PerfilAmigoFragment",error.toString()); }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void cargarListas() {
        if (tipo == tipoCancion) {
            adapterListasCanciones = new ListaCancionesAdapter(listasCanciones);
            recyclerListasCancionesAmigo.setAdapter(adapterListasCanciones);

            adapterListasCanciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CancionesListaActivity.class);
                    Bundle b = new Bundle();
                    b.putString("nombre", amigoDto.getLista_cancion().get(
                            recyclerListasCancionesAmigo.getChildAdapterPosition(v)).getNombre());
                    b.putInt("idLista", amigoDto.getLista_cancion().get(
                            recyclerListasCancionesAmigo.getChildAdapterPosition(v)).getId());
                    b.putSerializable("canciones", amigoDto.getLista_cancion().get(
                            recyclerListasCancionesAmigo.getChildAdapterPosition(v)).getCanciones());

                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        } else if (tipo == tipoPodcast) {
            adapterListasPodcasts = new ListaPodcastsAdapter(listasPodcasts);
            recyclerListasPodcastsAmigo.setAdapter(adapterListasPodcasts);

            adapterListasPodcasts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PodcastListaActivity.class);
                    Bundle b = new Bundle();
                    b.putString("nombre", amigoDto.getLista_podcast().get(
                            recyclerListasPodcastsAmigo.getChildAdapterPosition(v)).getNombre());
                    b.putInt("idLista", amigoDto.getLista_podcast().get(
                            recyclerListasPodcastsAmigo.getChildAdapterPosition(v)).getId());
                    b.putInt("indiceLista", recyclerListasPodcastsAmigo.getChildAdapterPosition(v));
                    b.putSerializable("podcasts", amigoDto.getLista_podcast().get(
                            recyclerListasPodcastsAmigo.getChildAdapterPosition(v)).getPodcasts());

                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_perfil_amigo, container, false);

        recyclerInfoAmigo = vista.findViewById(R.id.infoAmigo);
        recyclerInfoAmigo.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recyclerListasCancionesAmigo = vista.findViewById(R.id.listasAmigo);
        recyclerListasCancionesAmigo.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));

        recyclerListasPodcastsAmigo = vista.findViewById(R.id.listasAmigo);
        recyclerListasPodcastsAmigo.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));

        btnCancionesPodcast = vista.findViewById(R.id.cambiarMusicaPodcast);

        return vista;
    }
}
