package com.example.carolshaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.Principal;

public class MainLogged extends AppCompatActivity {

    private boolean primeraVez = true;
    private Fragment firstFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);
        if(primeraVez){
            this.firstFragment = new PrincipalFragment();
            primeraVez = false;
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,firstFragment).commit();

        }else{
            //Para que entre directamente al fragmento principal
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);//findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
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
                            case R.id.nav_profile:
                                selectedFragment = new SocialFragment();
                                break;
                            default:
                                selectedFragment = new PrincipalFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return true;
                }
            };

}
