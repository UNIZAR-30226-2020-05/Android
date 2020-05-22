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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.carolshaw.objetos.Album;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.Podcast;

import java.io.IOException;
import java.util.ArrayList;

import static android.media.MediaPlayer.create;

public class ReproductorFragment extends Fragment {

    public static final int TIPO_CANCION = 0;
    public static final int TIPO_PODCAST = 1;
    private static final String LISTA_CANCIONES = "lista_canciones";
    private static final String LISTA_PODCASTS = "lista_podcasts";
    private static final String TIPO = "tipo";

    private String URL_API;
    private ImageButton play_pause;
    private MediaPlayer mediaPlayer;
    private ImageView caratula;
    private SeekBar seekBar;

    private Runnable runnable;
    private int posicion = 0; //indice del vector
    private int tipo;

    ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
    ArrayList<MediaPlayer> listaStreaming = new ArrayList<MediaPlayer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);

        Bundle b = getActivity().getIntent().getExtras();
        if (b != null) {
            int tipo = b.getInt("tipo");
            if (tipo == TIPO_CANCION) {
                canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
            } else if (tipo == TIPO_PODCAST) {
                podcasts =  (ArrayList<Podcast>) b.getSerializable("podcasts");
            }
        }
        Log.d("mainlogged","----dentro create: " + canciones.get(0).getName());
        //TODO: Mover el mediaPlayer a un 'servicio' para que pueda ejecutarse en segundo plano o con new Thread(obj).start
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(URL_API + "/song/play/" + canciones.get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK); //Evita que el CPU se apague por ahorro de energía
        WifiManager.WifiLock wifiLock = ((WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock"); //Evita que el wifi se apague por ahorro de energía
        //wifiLock.release(); cuando se haga stop()

        wifiLock.acquire();
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mediaPlayer.start();
                actualizarSeekBar();
            }
        });
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
        caratula = vista.findViewById(R.id.imageView2);
        seekBar = vista.findViewById(R.id.seekBar);
        return vista;
    }


/*
    public void PlayPause(View view){
        if(listaStreaming[posicion].isPlaying()){ //verifica que cancion del vector esta sonando
            listaStreaming[posicion].pause();
            play_pause.setBackgroundResource(R.drawable.play);
            Toast.makeText(this.getContext(),"Pausa",Toast.LENGTH_SHORT).show();
        }
        else{
            vector[posicion].start();
            play_pause.setBackgroundResource(R.drawable.pause);
            Toast.makeText(this.getContext(),"Play",Toast.LENGTH_SHORT).show();
        }
    }

   /* public void Siguiente(View view){
        if(posicion < vector.length-1){
            if(vector[posicion].isPlaying()){
                vector[posicion].stop();
                posicion++;
                vector[posicion].start();
                for(int i=0;i<lista.length;i++){ //para cambiar portada
                    if(posicion == i){
                        //llamar back con nombre album
                        iv.setImageURI(lista[i].cancion.url);
                    }
                }
            }
            else{ posicion++;
                for(int i=0;i<lista.length;i++){ //para cambiar portada
                    if(posicion == i){
                        iv.setImageURI(lista[i].cancion.url);
                    }
                }
            }
        }
        else{ Toast.makeText(this,"No hay más canciones",Toast.LENGTH_SHORT).show();}
    }

    public void Anterior(View view) {
        if (posicion >= 1) {
            if (vector[posicion].isPlaying()) {
                vector[posicion].stop();
                for(int i=0; i < lista.lenght ; i++){ //se vuelve a poner porque hay posibilidad
                    //del que el stop pierda la cancion que se estaba reproduciendo
                    vector[i] = MediaPlayer.create(this,lista.cancio.url);
                }
                posicion--;
                vector[posicion].start();
                for (int i = 0; i < lista.length; i++) { //para cambiar portada
                    if (posicion == i) {
                        iv.setImageURI(lista[i].cancion.url);
                    }
                }
            } else {
                posicion--;
                for (int i = 0; i < lista.length; i++) { //para cambiar portada
                    if (posicion == i) {
                        iv.setImageURI(lista[i].cancion.url);
                    }
                }
            }
        } else {
            Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show();
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
