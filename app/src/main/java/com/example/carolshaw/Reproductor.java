package com.example.carolshaw;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Reproductor extends AppCompatActivity implements MediaPlayerControl {
    ImageButton play_pause;
    MediaPlayer mp;
    ImageView iv; //foto de portada
    int posicion =0; //indice del vector
    int tipo;

    ArrayList<MediaPlayer> listaCancion = new ArrayList<MediaPlayer>; //poner el lengt de la lista de nuemero canciones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        play_pause = (ImageButton)findViewById(R.id.play);
        iv = (ImageView)findViewById(R.id.imageView2);

        for(int i=0; i < lista.lenght ; i++){
            //llamar al back, distinguir cancion/podcast
            listaCancion.get(i)= MediaPlayer.create(this,lista.cancio.url);
        }


    }

    public void PlayPause(View view){
        if(vector[posicion].isPlaying()){ //verifica que cancion del vector esta sonando
            vector[posicion].pause();
            play_pause.setBackgroundResource(R.drawable.play);
            Toast.makeText(this,"Pausa",Toast.LENGTH_SHORT).show();
        }
        else{
            vector[posicion].start();
            play_pause.setBackgroundResource(R.drawable.pause);
            Toast.makeText(this,"Play",Toast.LENGTH_SHORT).show();
        }
    }

    public void Siguiente(View view){
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
