package com.example.carolshaw;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.ListaPodcastsAdapter;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class AnadirPodcastALista extends Fragment {

    private static final String ID_PODCAST = "id_podcast";
    private int idPodcast;

    private RecyclerView recycler;
    private ListaPodcastsAdapter adapter;
    private UsuarioDto usuarioLog;
    private FloatingActionButton crearNuevaLista;
    private String URL_API;
    private String nombreLista;

    public AnadirPodcastALista() {
        // Required empty public constructor
    }

    public static AnadirPodcastALista newInstance(int idPodcast) {
        AnadirPodcastALista fragment = new AnadirPodcastALista();
        Bundle args = new Bundle();
        args.putInt(ID_PODCAST, idPodcast);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPodcast = getArguments().getInt(ID_PODCAST);
        }
        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                int p = recycler.getChildAdapterPosition(v);
                anadirPodcast(idPodcast,usuarioLog.getLista_podcast().get(p).getId(),p);

            }
        });

        crearNuevaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Que pasa cuando se toca encima del amigo
                nuevaLista();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_anadir_podcast_a_lista, container, false);
        recycler = vista.findViewById(R.id.recyclerViewListasPodcasts);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        adapter = new ListaPodcastsAdapter(usuarioLog.getLista_podcast());
        recycler.setAdapter(adapter);
        crearNuevaLista = vista.findViewById(R.id.btnAnadirLista);
        return vista;
    }

    private void anadirPodcast(final int idPodcast, final int idLista, final int indiceLista) {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = URL_API + "/listaPodcast/add/" + idLista;
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaPodcast obj = gson.fromJson(response.toString(), ListaPodcast.class);
                        usuarioLog.deleteLista_podcast(indiceLista);
                        usuarioLog.addLista_podcast(obj);
                        informar("Podcast añadida a ", obj.getNombre());
                        finalizarFragmento();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("El podcast ya se encuentra en la lista");
                        finalizarFragmento();
                    }
                }) {
            @Override
            public byte[] getBody() {
                return String.valueOf(idPodcast).getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    //Función del botón añadir nueva lista
    public void nuevaLista() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nombre de la lista");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombreLista = input.getText().toString();
                crearNuevaLista();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Manda la petición al back para crear la lista
    private void crearNuevaLista() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        JSONObject params = new JSONObject();
        String urlPost = URL_API + "/listaPodcast/create";
        // Adding parameters to request
        try {
            params.put("id_usuario", usuarioLog.getId());
            params.put("nombre", nombreLista);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlPost, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("AnadirPodcastsALista", "response: " + response.toString());
                        try {
                            ListaPodcast listaPodcast = new ListaPodcast(response.getInt("id"),
                                    response.getInt("id_usuario"),response.getString("nombre"));
                            usuarioLog.addLista_podcast(listaPodcast);
                            cargarListas();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AnadirCancionesALista", "error: " + error.toString());
                        Toast.makeText(getContext(), "Error al cargar las listas de Podcasts", Toast.LENGTH_SHORT).show();
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    //Carga las listas del usuario
    private void cargarListas() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(adapter);
    }

    private void finalizarFragmento() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new BusquedaFragment()).commit();
    }

    /* informa mediante un TOAST
     */
    private void informar(String mensaje, String nombreLista) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                mensaje + nombreLista, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
    /* informa mediante un TOAST
     */
    private void informar(String mensaje) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                mensaje, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
