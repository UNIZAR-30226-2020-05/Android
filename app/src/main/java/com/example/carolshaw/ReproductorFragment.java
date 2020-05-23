package com.example.carolshaw;

import androidx.fragment.app.Fragment;

import android.content.Context;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.Podcast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.MediaPlayer.SEEK_CLOSEST_SYNC;
import static android.media.MediaPlayer.create;

public class ReproductorFragment extends Fragment {

    public static final int TIPO_CANCION = 0;
    public static final int TIPO_PODCAST = 1;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private int tipo;

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

    private Runnable runnable;
    private int indiceReproduccion; //indice del vector

    ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    ArrayList<Podcast> podcasts = new ArrayList<Podcast>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK); //Evita que el CPU se apague por ahorro de energía
        WifiManager.WifiLock wifiLock = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock"); //Evita que el wifi se apague por ahorro de energía
        //TODO: wifiLock.release(); cuando se haga stop()

        wifiLock.acquire();

        Bundle b = getActivity().getIntent().getExtras();
        reiniciarMediaPlayer();

        if (b != null) {
            tipo = b.getInt("tipo");
            indiceReproduccion = 0;
            if (tipo == TIPO_CANCION) {
                canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
                bajarCaratulaCancion(indiceReproduccion);
            } else if (tipo == TIPO_PODCAST) {
                podcasts =  (ArrayList<Podcast>) b.getSerializable("podcasts");
                caratula.setImageResource(R.drawable.podcast1);
                reproducirPodcasts(indiceReproduccion);
            }
        } else {
            informar("hay que poner la ultima cancion del userlog global");
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("Reproductor","cancion terminada");
                if (tipo == TIPO_CANCION) {
                    if (indiceReproduccion < canciones.size() - 1) {
                        bajarCaratulaCancion(indiceReproduccion);
                    }
                } else if (tipo == TIPO_PODCAST) {
                    if (indiceReproduccion < podcasts.size() - 1) {
                        reproducirPodcasts(indiceReproduccion);
                    }
                }
            }

        });

        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int position = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Log.d("seekbar", String.valueOf(progress));
                    position = progress;
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    Log.d("seekbar", "progres:" + String.valueOf(seekBar.getProgress()));
                }
            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer arg0) {
                Log.d("seekbar","completado");
                mediaPlayer.start();
            }
        });*/
    }

    private void bajarCaratulaCancion(final int id) {
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
                                reproducirCanciones(id);
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
                    play_pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    int duracionPodcast = podcasts.get(indiceReproduccion).getDuracion(); //en segundos
                    seekBar.setMax(duracionPodcast * 1000);
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
                    play_pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    int duracionCancion = canciones.get(indiceReproduccion).getDuracion(); //en segundos
                    seekBar.setMax(duracionCancion * 1000);
                    actualizarSeekBar();
                }
            });
            actualizarDatosVista();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Actualiza la barra cada segundo
    private void actualizarSeekBar () {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        int tiempo = mediaPlayer.getCurrentPosition() / 1000;
        tiempoActual.setText(String.format("%02d:%02d", tiempo / 60, tiempo % 60));
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

        return vista;
    }

    private void actualizarDatosVista () {
        //TODO: no se por que el tiempoTotal y titulo lo coge como null y no puede poner texto en ellos
        //Poner caratula (hacer busqueda del album con el nombre de la cancion), podcast siempre tiene la misma


        if (tipo == TIPO_CANCION) {
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
            Podcast podcast = podcasts.get(indiceReproduccion);
            tiempoTotal.setText(podcast.getDuracionMMSS());
            titulo.setText(podcast.getName());
            autor.setText(podcast.getArtista());
        }
    }

    private void previo () {
        if (tipo == TIPO_CANCION) {
            if (indiceReproduccion > 0) {

                indiceReproduccion--;
                bajarCaratulaCancion(indiceReproduccion);
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
                bajarCaratulaCancion(indiceReproduccion);
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
