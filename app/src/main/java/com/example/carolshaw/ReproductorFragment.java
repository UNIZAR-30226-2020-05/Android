package com.example.carolshaw;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.Podcast;

import java.io.IOException;
import java.util.ArrayList;

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

    private Runnable runnable;
    private int indiceReproduccion; //indice del vector

    ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    ArrayList<Podcast> podcasts = new ArrayList<Podcast>();

    //TODO: Mover el mediaPlayer a un 'servicio' para que pueda ejecutarse en segundo plano o con new Thread(obj).start
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Bundle b = getActivity().getIntent().getExtras();
        reiniciarMediaPlayer();

        if (b != null) {
            tipo = b.getInt("tipo");
            indiceReproduccion = 0;
            if (tipo == TIPO_CANCION) {
                canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
                reproducirCanciones(indiceReproduccion);
            } else if (tipo == TIPO_PODCAST) {
                podcasts =  (ArrayList<Podcast>) b.getSerializable("podcasts");
                reproducirPodcasts(indiceReproduccion);
            }
        } else {
            informar("hay que poner la ultima cancion del userlog global");
        }
    }

    private void reiniciarMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }
    }

    private void reproducirPodcasts(final int id) {
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
                    actualizarSeekBar();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reproducirCanciones(final int id) {
        try {
            mediaPlayer.setDataSource(URL_API + "/song/play/" + canciones.get(id).getName());
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
                    actualizarSeekBar();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarSeekBar() {
        int duracionCancion = 231; //en segundos
        seekBar.setMax(duracionCancion*1000);
        seekBar.setProgress(mediaPlayer.getCurrentPosition(),true);
        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    actualizarSeekBar();
                }
            };

            Handler handler = new Handler();
            handler.postDelayed(runnable,1000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_reproductor, container, false);
        play_pause = vista.findViewById(R.id.play);
        previo = vista.findViewById(R.id.previo);
        siguiente = vista.findViewById(R.id.next);
        caratula = vista.findViewById(R.id.imageView2);
        seekBar = vista.findViewById(R.id.seekBar);
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

    private void previo() {
        if (tipo == TIPO_CANCION) {
            if(indiceReproduccion > 0){
                reiniciarMediaPlayer();
                indiceReproduccion--;
                reproducirCanciones(indiceReproduccion);
            } else {
                informar("Esta es la primera canción de la lista de reproducción");
            }
        } else if (tipo == TIPO_PODCAST) {
            if(indiceReproduccion > 0){
                reiniciarMediaPlayer();
                indiceReproduccion--;
                reproducirPodcasts(indiceReproduccion);
            } else {
                informar("Este es el primer podcast de la lista de reproducción");
            }
        }
    }

    private void siguiente() {
        if (tipo == TIPO_CANCION) {
            if(indiceReproduccion < canciones.size()-1){
                reiniciarMediaPlayer();
                indiceReproduccion++;
                reproducirCanciones(indiceReproduccion);
            } else {
                informar("No hay más canciones en la lista de reproducción");
            }
        } else if (tipo == TIPO_PODCAST) {
            if(indiceReproduccion < podcasts.size()-1){
                reiniciarMediaPlayer();
                indiceReproduccion++;
                reproducirPodcasts(indiceReproduccion);
            } else {
                informar("No hay más podcasts en la lista de reproducción");
            }
        }
    }


    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play_pause.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.pause);
        }
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
