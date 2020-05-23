package com.example.carolshaw;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.Podcast;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.MediaPlayer.SEEK_CLOSEST_SYNC;
import static android.media.MediaPlayer.create;

public class ReproductorFragment extends Fragment {

    public static final int TIPO_CANCION = 0;
    public static final int TIPO_PODCAST = 1;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static int tipo;

    private String URL_API;
    private ImageButton play_pause;
    private ImageButton previo;
    private ImageButton siguiente;
    private ImageView caratula;
    private SeekBar seekBar;
    private TextView tiempoTotal;
    private TextView tiempoActual;
    private TextView titulo;
    private TextView autor;
    private ImageView corazonFavorito;

    private Runnable runnable;
    private static int indiceReproduccion; //indice del vector

    private static ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    private static ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
    private UsuarioDto usuarioLog;
    private static boolean primeraVez = true;
    private static boolean estabaSonando = false;
    private boolean nuevo = false;
    private static int tempUltimaReproduccion = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usuarioLog = (UsuarioDto) getActivity().getApplicationContext();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK); //Evita que el CPU se apague por ahorro de energía
        WifiManager.WifiLock wifiLock = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock"); //Evita que el wifi se apague por ahorro de energía

        wifiLock.acquire();

        if (primeraVez) {
            //prepara para reproducir la ultima cancion/podcast del usuario
            tipo = usuarioLog.getTipo_ultima_reproduccion();
            Log.d("reproductor","primera vez");
        }

        Bundle b = getActivity().getIntent().getExtras();

        if (b != null) {
            nuevo = b.getBoolean("nuevo");
        }
        if (!estabaSonando || primeraVez || nuevo) {
            primeraVez = false;
            reiniciarMediaPlayer();

            if (b != null) {
                getActivity().getIntent().removeExtra("nuevo");
                tipo = b.getInt("tipo");
                indiceReproduccion = 0;
                if (tipo == TIPO_CANCION) {
                    canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
                    bajarCaratulaCancion(indiceReproduccion,true);
                } else if (tipo == TIPO_PODCAST) {
                    podcasts = (ArrayList<Podcast>) b.getSerializable("podcasts");
                    caratula.setImageResource(R.drawable.podcast1);
                    reproducirPodcasts(indiceReproduccion);
                }
            } else {
                if (usuarioLog.getTipo_ultima_reproduccion() == TIPO_CANCION) {
                    descargarUltimaCancion();
                } else if (usuarioLog.getTipo_ultima_reproduccion() == TIPO_PODCAST) {
                    descargarUltimoPodcast();
                }
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.d("Reproductor", "cancion terminada");
                    if (tipo == TIPO_CANCION) {
                        if (indiceReproduccion < canciones.size() - 1) {
                            bajarCaratulaCancion(indiceReproduccion,true);
                        }
                    } else if (tipo == TIPO_PODCAST) {
                        if (indiceReproduccion < podcasts.size() - 1) {
                            reproducirPodcasts(indiceReproduccion);
                        }
                    }
                }

            });

        } else {
            actualizarDatosVista();
            if (tipo == TIPO_CANCION) {
                seekBar.setMax(canciones.get(indiceReproduccion).getDuracion()*1000);
            } else if (tipo == TIPO_PODCAST) {
                seekBar.setMax(podcasts.get(indiceReproduccion).getDuracion()*1000);
            }
            actualizarSeekBar();
        }
    }

    private void descargarUltimoPodcast() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/podcast/getByName?name=";

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Podcast> podcastsEncontrados = new ArrayList<Podcast>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Gson gson = new Gson();
                                Podcast obj = gson.fromJson(response.getJSONObject(i).toString(), Podcast.class);
                                podcastsEncontrados.add(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for (Podcast podcast : podcastsEncontrados) {
                            if (podcast.getId() == usuarioLog.getId_ultima_reproduccion()) {
                                indiceReproduccion = 0;
                                podcasts.add(podcast);
                                reproducirPodcasts(indiceReproduccion);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error desconocido al obtener la ultima canción");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void descargarUltimaCancion() {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/song/getByName?name=";

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Cancion> cancionesEncontradas = new ArrayList<Cancion>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Gson gson = new Gson();
                                Cancion obj = gson.fromJson(response.getJSONObject(i).toString(), Cancion.class);
                                cancionesEncontradas.add(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        for (Cancion cancion : cancionesEncontradas) {
                            if (cancion.getId() == usuarioLog.getId_ultima_reproduccion()) {
                                indiceReproduccion = 0;
                                canciones.add(cancion);
                                bajarCaratulaCancion(indiceReproduccion,true);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error desconocido al obtener la ultima canción");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void bajarCaratulaCancion(final int id, final boolean reproducir) {
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        String urlGet = URL_API + "/album/getByTitulo?titulo=" + canciones.get(id).getAlbum();

        // Creating a JSON Object request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Gson gson = new Gson();
                                Album obj = gson.fromJson(response.getJSONObject(i).toString(), Album.class);
                                Glide.with(getContext()).load(URL_API + obj.getCaratula()).
                                        into(caratula);
                                if (reproducir) {
                                    reproducirCanciones(id);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ReproductorFragment", "error: " + error.toString());
                        informar("Error desconocido");
                    }
                });
        // Adding the string request to the queue
        rq.add(jsonArrayRequest);
    }

    private void reiniciarMediaPlayer () {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
    }

    private void reproducirPodcasts ( final int id){
        reiniciarMediaPlayer();
        try {
            mediaPlayer.setDataSource(URL_API + "/podcast/play/" + podcasts.get(id).getName());
            mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK); //Evita que el CPU se apague por ahorro de energía
            WifiManager.WifiLock wifiLock = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock"); //Evita que el wifi se apague por ahorro de energía
            //TODO: wifiLock.release(); cuando se haga stop()

            wifiLock.acquire();
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    estabaSonando = true;
                    play_pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    int duracionPodcast = podcasts.get(indiceReproduccion).getDuracion(); //en segundos
                    seekBar.setMax(duracionPodcast * 1000);
                    establecerUltimoPodcast();
                    actualizarSeekBar();
                }
            });
            actualizarDatosVista();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reproducirCanciones(final int id) {
        reiniciarMediaPlayer();
        try {
            mediaPlayer.setDataSource(URL_API + "/song/play/" + canciones.get(id).getName());

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    estabaSonando = true;
                    play_pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    int duracionCancion = canciones.get(indiceReproduccion).getDuracion(); //en segundos
                    seekBar.setMax(duracionCancion * 1000);
                    establecerUltimaCancion();
                    actualizarSeekBar();
                }
            });
            actualizarDatosVista();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void establecerUltimoPodcast() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API +"/user/modifyLastPlayAndroid/" + usuarioLog.getId();

        final String params = podcasts.get(indiceReproduccion).getId() + ";" +
                mediaPlayer.getCurrentPosition()/1000 + ";" +
                TIPO_PODCAST;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reproductor","Guardado ultimo podcast");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("Reproductor",error.toString()); }
                }) {
            @Override
            public byte[] getBody() {
                return params.getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void establecerUltimaCancion() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API +"/user/modifyLastPlayAndroid/" + usuarioLog.getId();

        final String params = canciones.get(indiceReproduccion).getId() + ";" +
            mediaPlayer.getCurrentPosition()/1000 + ";" +
            TIPO_CANCION;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, peticion, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Reproductor","Guardada ultima cancion");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { Log.d("Reproductor",error.toString()); }
                }) {
            @Override
            public byte[] getBody() {
                return params.getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    //Actualiza la barra cada segundo
    private void actualizarSeekBar () {
        int tiempoTotal = seekBar.getMax()/1000;
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        int tiempo = mediaPlayer.getCurrentPosition() / 1000;
        tiempoActual.setText(String.format("%02d:%02d", tiempo / 60, tiempo % 60));
        if (tiempoTotal == tiempo) {
            siguiente();
        }
        if (tempUltimaReproduccion >= 30) { //cada 30s guarda el punto de la ultima reproduccion
            tempUltimaReproduccion = 0;
            if (tipo == TIPO_CANCION) {
                establecerUltimaCancion();
            } else if (tipo == TIPO_PODCAST) {
                establecerUltimoPodcast();
            }
        } else {
            tempUltimaReproduccion++;
        }
        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    actualizarSeekBar();
                }
            };

            Handler handler = new Handler();
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        View vista = inflater.inflate(R.layout.fragment_reproductor, container, false);
        play_pause = vista.findViewById(R.id.play);
        previo = vista.findViewById(R.id.previo);
        siguiente = vista.findViewById(R.id.next);
        caratula = vista.findViewById(R.id.imageView2);
        seekBar = vista.findViewById(R.id.seekBar);
        tiempoActual = vista.findViewById(R.id.progreso_actual);
        tiempoTotal = vista.findViewById(R.id.duracionTotal);
        titulo = vista.findViewById(R.id.nombre);
        autor = vista.findViewById(R.id.autor);
        corazonFavorito = vista.findViewById(R.id.corazonFavorito);

        if (mediaPlayer.isPlaying()) {
            play_pause.setImageResource(R.drawable.pause);
        } else {
            play_pause.setImageResource(R.drawable.play);
        }

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });
        previo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previo();
            }
        });
        corazonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirFavorito();
            }
        });

        return vista;
    }

    private void actualizarDatosVista () {

        if (tipo == TIPO_CANCION) {
            bajarCaratulaCancion(indiceReproduccion,false);
            Cancion cancion = canciones.get(indiceReproduccion);
            tiempoTotal.setText(cancion.getDuracionMMSS());
            titulo.setText(cancion.getName());
            String autorString = "";
            ArrayList<String> artistas = new ArrayList<String>();
            artistas = cancion.getArtistas();
            for (int i = 0; i < artistas.size(); i++) {
                autorString += artistas.get(i) + "\n";
            }
            autor.setText(autorString);
        } else if (tipo == TIPO_PODCAST) {
            caratula.setImageResource(R.drawable.podcast1);
            Podcast podcast = podcasts.get(indiceReproduccion);
            tiempoTotal.setText(podcast.getDuracionMMSS());
            titulo.setText(podcast.getName());
            autor.setText(podcast.getArtista());
        }
    }

    private void anadirFavorito() {
        if (tipo == TIPO_CANCION) {
            buscarFavoritosCancion();
        } else if (tipo == TIPO_PODCAST) {
            buscarFavoritosPodcast();
        }
    }

    private void buscarFavoritosPodcast() {
        ArrayList<ListaPodcast> listasUsuario = usuarioLog.getLista_podcast();
        boolean encontrado = false;
        for (int i = 0; i < listasUsuario.size(); i++) {
            if (listasUsuario.get(i).getNombre().equals("Favoritos")) {
                anadirPodcastFavorito(listasUsuario.get(i).getId(), i);
                i = listasUsuario.size(); //Garantiza terminar el bucle
                encontrado = true;
            }
        }
        if (!encontrado) {
            informar("Lista de Favoritos no encontrada");
        }
    }

    private void buscarFavoritosCancion() {

        ArrayList<ListaCancion> listasUsuario = usuarioLog.getLista_cancion();
        boolean encontrado = false;
        for (int i = 0; i < listasUsuario.size(); i++) {
            if (listasUsuario.get(i).getNombre().equals("Favoritos")) {
                anadirCancionFavorita(listasUsuario.get(i).getId(),i);
                i = listasUsuario.size(); //Garantiza terminar el bucle
                encontrado = true;
            }
        }
        if (!encontrado) {
            informar("Lista de Favoritos no encontrada");
        }
    }

    private void anadirPodcastFavorito(final int idLista, final int indiceLista) {
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
                        informar("Podcast añadido a favoritos");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error al añadir el podcast a favoritos");
                    }
                }) {
            @Override
            public byte[] getBody() {
                return String.valueOf(podcasts.get(indiceReproduccion).getId()).getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void anadirCancionFavorita(final int idLista, final int indiceLista) {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = URL_API + "/listaCancion/add/" + idLista;
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        ListaCancion obj = gson.fromJson(response.toString(), ListaCancion.class);
                        usuarioLog.deleteLista_cancion(indiceLista);
                        usuarioLog.addLista_cancion(obj);
                        informar("Canción añadida a favoritos");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informar("Error al añadir la canción a favoritos");
                    }
                }) {
            @Override
            public byte[] getBody() {
                return String.valueOf(canciones.get(indiceReproduccion).getId()).getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void previo () {
        if (tipo == TIPO_CANCION) {
            if (indiceReproduccion > 0) {

                indiceReproduccion--;
                bajarCaratulaCancion(indiceReproduccion,true);
            } else {
                informar("Esta es la primera canción de la lista de reproducción");
            }
        } else if (tipo == TIPO_PODCAST) {
            if (indiceReproduccion > 0) {
                indiceReproduccion--;
                reproducirPodcasts(indiceReproduccion);
            } else {
                informar("Este es el primer podcast de la lista de reproducción");
            }
        }
    }

    private void siguiente () {
        if (tipo == TIPO_CANCION) {
            if (indiceReproduccion < canciones.size() - 1) {
                indiceReproduccion++;
                bajarCaratulaCancion(indiceReproduccion,true);
            } else {
                informar("No hay más canciones en la lista de reproducción");
            }
        } else if (tipo == TIPO_PODCAST) {
            if (indiceReproduccion < podcasts.size() - 1) {
                indiceReproduccion++;
                reproducirPodcasts(indiceReproduccion);
            } else {
                informar("No hay más podcasts en la lista de reproducción");
            }
        }
    }


    public void playPause () {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play_pause.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            actualizarSeekBar();
            play_pause.setImageResource(R.drawable.pause);
        }
    }

    /* informa mediante un TOAST
     */
    private void informar (String mensaje){
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                mensaje, Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}
