package com.example.carolshaw;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.CancionesListaAdapter;
import com.example.carolshaw.adapters.ResultadoPodcastsBusquedaAdapter;
import com.example.carolshaw.objetos.Podcast;
import com.example.carolshaw.objetos.UsuarioDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PodcastListaActivity extends AppCompatActivity {

    private ArrayList<Podcast> podcasts = null;
    private String nombreLista;
    private TextView tituloVista;
    private ImageView botonBorrar;
    private ImageView botonPlay;
    private TextView copiarLista;
    private RecyclerView recycler;
    private ResultadoPodcastsBusquedaAdapter adapter;
    private String URL_API;
    private int idLista;
    private UsuarioDto usuarioLog;
    private int indiceInternoLista;
    private boolean perteneceUsuario = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_lista);
        tituloVista = findViewById(R.id.tituloLista);
        botonBorrar = findViewById(R.id.btnBorrarLista);
        copiarLista = findViewById(R.id.codigoLista);
        recycler = findViewById(R.id.recyclerViewPodcasts);
        botonPlay = findViewById(R.id.btnPlayLista);

        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getApplicationContext();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            podcasts = (ArrayList<Podcast>) b.getSerializable("podcasts");
            nombreLista = b.getString("nombre");
            idLista = b.getInt("idLista");
            copiarLista.setText(String.valueOf(idLista));
            tituloVista.setText(nombreLista);
            for (int i = 0; i < usuarioLog.getLista_podcast().size(); i++) {
                if(usuarioLog.getLista_podcast().get(i).getId() == idLista){
                    perteneceUsuario = true;
                    indiceInternoLista = i;
                }
            }
            adapter = new ResultadoPodcastsBusquedaAdapter(podcasts, perteneceUsuario);
            recycler.setLayoutManager(new LinearLayoutManager(PodcastListaActivity.this,
                    LinearLayoutManager.VERTICAL,false));
            recycler.setAdapter(adapter);

            adapter.setOnItemClickListener(new ResultadoPodcastsBusquedaAdapter.OnItemClickListener() {
                @Override
                public void onDeleteClick(int position) {
                    confirmarBorrarCancion(podcasts.get(position).getId(), position);
                }
            });
        }

        if(perteneceUsuario){
            //Evita que se borre si es el de favoritos
            if (nombreLista.equals("Favoritos")) {
                copiarLista.setVisibility(View.GONE);
                botonBorrar.setVisibility(View.GONE);
            }

            botonBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmarBorrado();
                }
            });
        } else {
            botonBorrar.setVisibility(View.GONE);
        }

        copiarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codigo lista", copiarLista.getText().toString());
                clipboard.setPrimaryClip(clip);
                informar("Código copiado");
            }
        });

        if (podcasts.size() > 0) {
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PodcastListaActivity.this, MainLogged.class);
                    Bundle b = new Bundle();
                    ArrayList<Podcast> podcastsReproductor = new ArrayList<Podcast>();
                    podcastsReproductor.add(podcasts.get(recycler.getChildAdapterPosition(v))); //Añade la cancion correspondiente
                    b.putSerializable("podcasts", podcastsReproductor);
                    b.putInt("tipo",ReproductorFragment.TIPO_PODCAST);
                    b.putBoolean("nuevo",true);
                    intent.putExtras(b);
                    finish();
                    startActivity(intent);
                }
            });

            botonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PodcastListaActivity.this, MainLogged.class);
                    Bundle b = new Bundle();
                    b.putSerializable("podcasts", podcasts);
                    b.putInt("tipo",ReproductorFragment.TIPO_PODCAST);
                    b.putBoolean("nuevo",true);
                    intent.putExtras(b);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    private void borrarLista() {
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        String url = URL_API + "/listaPodcast/delete/" + idLista;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        usuarioLog.deleteLista_podcast(indiceInternoLista);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        usuarioLog.deleteLista_podcast(indiceInternoLista);
                        finish();
                        //startActivity(new Intent(CancionesListaActivity.this, ListaCancionesActivity.class));
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void confirmarBorrado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Seguro que dese eliminar la lista?");

        // Set up the buttons
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarLista();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void confirmarBorrarCancion(final int id, final int indicePodcast) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Seguro que desea eliminar el podcast?");

        // Set up the buttons
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarCancion(id,indicePodcast);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void borrarCancion(final int id, final int indicePodcast){
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = URL_API + "/listaPodcast/deletePodcast/" + idLista;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        informar(podcasts.get(indicePodcast).getName() + " eliminado de la lista");
                        adapter.notifyItemRemoved(indicePodcast);
                        podcasts.remove(indicePodcast);

                        //Elimina la cancion de la lista correspondiente del propio usuario para que no vuelva a aparecer
                        usuarioLog.getLista_podcast().get(indiceInternoLista).getPodcasts().remove(indicePodcast);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error al borrar el podcast de la lista");
                        Log.d("CancionesListaActivity","error: " + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() {
                return String.valueOf(podcasts.get(indicePodcast).getId()).getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    /* informa mediante un TOAST
     */
    private void informar(String mensaje) {
        Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
