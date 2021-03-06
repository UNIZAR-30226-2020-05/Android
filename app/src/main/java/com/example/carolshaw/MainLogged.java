package com.example.carolshaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ReportFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.carolshaw.adapters.ResultadoCancionesBusquedaAdapter;
import com.example.carolshaw.objetos.Cancion;
import com.example.carolshaw.objetos.Podcast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainLogged extends AppCompatActivity {

    private boolean primeraVez = true;
    private Fragment firstFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);
        Bundle b = getIntent().getExtras();
        if(primeraVez && b == null){
            this.firstFragment = new PrincipalFragment();
            primeraVez = false;
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,firstFragment).commit();

        }else{
            //Para que entre directamente al fragmento principal
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);//findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
            if (b != null) {
                primeraVez = false;
                int tipo = b.getInt("tipo");
                if (tipo == ReproductorFragment.TIPO_CANCION) {
                    ArrayList<Cancion> canciones = (ArrayList<Cancion>) b.getSerializable("canciones");
                    b = new Bundle();
                    b.putSerializable("canciones", canciones);
                    b.putInt("tipo",tipo);
                    //b.putBoolean("nuevo",true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ReproductorFragment()).commit();
                } else if (tipo == ReproductorFragment.TIPO_PODCAST) {
                    ArrayList<Podcast> podcasts = (ArrayList<Podcast>) b.getSerializable("podcasts");
                    b = new Bundle();
                    b.putSerializable("podcasts", podcasts);
                    b.putInt("tipo",tipo);
                    //b.putBoolean("nuevo",true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ReproductorFragment()).commit();
                }
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = new PrincipalFragment();
                    if(primeraVez) {
                        primeraVez = false;
                    }else{
                        switch (menuItem.getItemId()) {
                            case R.id.nav_inicio:
                                selectedFragment = new PrincipalFragment();
                                break;
                            case R.id.nav_search:
                                selectedFragment = new BusquedaFragment();
                                break;
                            case R.id.nav_profile:
                                selectedFragment = new ModificarPerfilFragment();
                                break;
                            case R.id.nav_social:
                                selectedFragment = new PanelSocialFragment();
                                break;
                            case R.id.nav_player:
                                selectedFragment = new ReproductorFragment();
                                break;
                            default:
                                selectedFragment = new PrincipalFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lateral,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        Toast toast;
        View view;
        switch (item.getItemId()){
            case R.id.lista_musica:
                startActivity(new Intent(MainLogged.this, ListaCancionesActivity.class));
                return true;
            case R.id.lista_podcast:
                startActivity(new Intent(MainLogged.this, ListaPodcastsActivity.class));
                return true;
            case R.id.cerrar_sesion:
                toast = Toast.makeText(getApplicationContext(), "Adios!", Toast.LENGTH_SHORT);
                view = toast.getView();

                //Cambiar color del fonto
                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                toast.show();
                //startActivity(new Intent(MainLogged.this, LoginActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

}
