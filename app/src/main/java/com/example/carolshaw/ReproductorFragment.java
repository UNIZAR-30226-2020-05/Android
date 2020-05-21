package com.example.carolshaw;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.ListaPodcast;
import com.example.carolshaw.objetos.Podcast;
import com.example.carolshaw.objetos.UsuarioDto;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.media.MediaPlayer.create;

public class ReproductorFragment extends Fragment implements MediaPlayerControl {

    public static final int TIPO_CANCION = 0;
    public static final int TIPO_PODCAST = 1;
    private static final String LISTA = "lista";
    private static final String TIPO = "tipo";

    String URL_API;
    ImageButton play_pause;
    MediaPlayer mp;
    ImageView iv; //foto de portada
    int posicion = 0; //indice del vector
    int tipo;

    ListaCancion listaCancion;
    ArrayList<Cancion> canciones = new ArrayList<Cancion>();
    ListaPodcast listaPodcast;
    ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
    ArrayList<MediaPlayer> listaStreaming = new ArrayList<MediaPlayer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL_API = getString(R.string.API);
        cargarPodcasts();
        /*if (getArguments() != null) {
            tipo = getArguments().getInt(TIPO);
            if(tipo == TIPO_CANCION){
                listaCancion = (ListaCancion) getArguments().getSerializable(LISTA);
                canciones = listaCancion.getCanciones();
                cargarCanciones();
            } else if (tipo == TIPO_PODCAST) {
                listaPodcast = (ListaPodcast) getArguments().getSerializable(LISTA);
                podcasts = listaPodcast.getPodcasts();
                cargarPodcasts();
            } else {
                informar("Error desconocido");
            }
        }

        }*/


    }

    /* NO FUNCIONA */
    private void cargarPodcasts() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API + "/podcast/play/The News - 11.05.20";

        StringRequest postRequest = new StringRequest(Request.Method.GET, peticion,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Reproductor", "respuesta: " + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Reproductor", error.toString());
                    }
                }
        );
        rq.add(postRequest);
    }

    private void cargarCanciones() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        String peticion = URL_API + "/podcast/play/Run To The Hills";

        StringRequest postRequest = new StringRequest(Request.Method.GET, peticion,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Reproductor", "respuesta: " + response);
                        crearListaMediaPlayer(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Reproductor", error.toString());
                    }
                }
        );
        rq.add(postRequest);
    }

    private void crearListaMediaPlayer( String url) {
        Uri myUri = Uri.parse(url);
        MediaPlayer aux = MediaPlayer.create(this.getContext(), myUri);
        listaStreaming.add(aux);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_reproductor, container, false);
        play_pause = vista.findViewById(R.id.play);
        iv = vista.findViewById(R.id.imageView2);
        return vista;
    }

    /*TODO: OJO, A LO MEJOR NO COGE BIEN EL ARRAYLIST DE OBJECT.
     * ALTERNATIVA: PASAR TRES PARAMETROS CANCIONES PODCAST Y TIPO Y EL QUE NO SE VAYA A USAR,
     * SE PASA COMO PARAMETRO DEL QUE NO SE USE UN NULL
     */
    public static AnadirCancionesALista newInstance(ArrayList<Object> lista, int tipo) {
        AnadirCancionesALista fragment = new AnadirCancionesALista();
        Bundle args = new Bundle();
        args.putSerializable(LISTA, lista);
        args.putInt(TIPO, tipo);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
