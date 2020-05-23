package com.example.carolshaw;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.adapters.CancionesListaAdapter;
import com.example.carolshaw.adapters.ListaCancionesAdapter;
import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.ListaCancion;
import com.example.carolshaw.objetos.UsuarioDto;

import org.json.JSONObject;

import java.util.ArrayList;

public class CancionesListaActivity extends AppCompatActivity {

    private UsuarioDto usuarioLog;
    private ArrayList<Cancion> canciones = null;
    private String nombreLista;
    private TextView tituloVista;
    private ImageView botonBorrar;
    private ImageView botonPlay;
    private TextView copiarLista;
    private RecyclerView recycler;
    private CancionesListaAdapter adapter;
    private String URL_API;
    private int idLista;
    private int indiceLista;
    private boolean perteneceUsuario = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones_lista);
        usuarioLog = (UsuarioDto) getApplicationContext();
        tituloVista = findViewById(R.id.tituloLista);
        botonBorrar = findViewById(R.id.btnBorrarLista);
        copiarLista = findViewById(R.id.codigoLista);
        recycler = findViewById(R.id.recyclerViewCanciones);
        botonPlay = findViewById(R.id.btnPlayLista);

        URL_API = getString(R.string.API);
        usuarioLog = (UsuarioDto) getApplicationContext();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
            nombreLista = b.getString("nombre");
            idLista = b.getInt("idLista");
            copiarLista.setText(String.valueOf(idLista));
            tituloVista.setText(nombreLista);
            adapter = new CancionesListaAdapter(canciones);
            recycler.setLayoutManager(new LinearLayoutManager(CancionesListaActivity.this,
                    LinearLayoutManager.VERTICAL,false));
            recycler.setAdapter(adapter);
            for (int i = 0; i < usuarioLog.getLista_cancion().size(); i++) {
                if(usuarioLog.getLista_cancion().get(i).getId() == idLista){
                    perteneceUsuario = true;
                    indiceLista = i;
                }
            }
        }
        if(perteneceUsuario){
            //Evita que se borre y copie si es el de favoritos
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

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancionesListaActivity.this, MainLogged.class);
                Bundle b = new Bundle();
                ArrayList<Cancion> cancionesReproductor = new ArrayList<Cancion>();
                cancionesReproductor.add(canciones.get(recycler.getChildAdapterPosition(v))); //Añade la cancion correspondiente
                b.putSerializable("canciones", cancionesReproductor);
                b.putInt("tipo",ReproductorFragment.TIPO_CANCION);
                b.putBoolean("nuevo",true);
                intent.putExtras(b);
                finish();
                startActivity(intent);
            }
        });

        botonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancionesListaActivity.this, MainLogged.class);
                Bundle b = new Bundle();
                b.putSerializable("canciones", canciones);
                b.putInt("tipo",ReproductorFragment.TIPO_CANCION);
                b.putBoolean("nuevo",true);
                intent.putExtras(b);
                finish();
                startActivity(intent);
            }
        });
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

    private void borrarLista() {
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        String url = URL_API + "/listaCancion/delete/" + idLista;

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CancionesListaActivity",response.toString());
                        usuarioLog.deleteLista_cancion(indiceLista);
                        finish();
                        //startActivity(new Intent(CancionesListaActivity.this, ListaCancionesActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("CancionesListaActivity","error: " + error.toString());
                        usuarioLog.deleteLista_cancion(indiceLista);
                        finish();
                        //startActivity(new Intent(CancionesListaActivity.this, ListaCancionesActivity.class));
                    }
                });

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
