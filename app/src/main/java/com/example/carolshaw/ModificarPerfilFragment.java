package com.example.carolshaw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carolshaw.objetos.UsuarioDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.Map;


public class ModificarPerfilFragment extends Fragment {
    private TextView nuevaContrasena;
    private TextView confirmarContrasena;
    private Button confirmar;
    private Button borrarUsuario;
    private String URL_API;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        URL_API = getString(R.string.API);
        nuevaContrasena = getView().findViewById(R.id.nuevaContrasena);
        confirmarContrasena = getView().findViewById(R.id.confirmarContrasena);
        confirmar = getView().findViewById(R.id.confirmar);
        borrarUsuario = getView().findViewById(R.id.borrar);

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compararContrasenas();
            }
        });

        borrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarUsuario();
            }
        });
    }

    private void borrarUsuario() {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        UsuarioDto usuarioDto = (UsuarioDto) getActivity().getApplicationContext();
        String url = URL_API + "/user/delete/" + usuarioDto.getId();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ModificarPerfil","Usuario eliminado");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informarUsuarioBorrado();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    private void compararContrasenas() {
        String nuevaContrasenaString = nuevaContrasena.getText().toString();
        String confirmarContrasenaString = confirmarContrasena.getText().toString();
        if(!nuevaContrasenaString.isEmpty() && !confirmarContrasenaString.isEmpty()) {
            if (!nuevaContrasenaString.equals(confirmarContrasenaString)) {
                informarContrasenasNoCoinciden();
            } else {
                String aux = (android.util.Base64.encodeToString(confirmarContrasenaString.getBytes(),
                        android.util.Base64.DEFAULT));
                guardarCambios(aux.substring(0, aux.length()-1));
            }
        } else {
            informarRellenarDatos();
        }
    }

    /* Guarda los cambios realizados por el usuario
     */
    private void guardarCambios(final String confirmarContrasenaString) {
        final RequestQueue rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        UsuarioDto usuarioDto = (UsuarioDto) getActivity().getApplicationContext();
        String url = URL_API + "/user/modifyPass/" + usuarioDto.getId();
        JSONObject params = new JSONObject();
        try {
            params.put("pass", confirmarContrasenaString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Creating a JSON Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ModificarPerfil","Contraseña modificada");
                        informarContrasenaCambiada();
                        getActivity().getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container,new PrincipalFragment()).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        informarFalloDesconocido();
                    }
                }) {
            @Override
            public byte[] getBody() {
                return confirmarContrasenaString.getBytes();
            }
        };

        // Adding the string request to the queue
        rq.add(jsonObjectRequest);
    }

    /* informa mediante un TOAST de que se ha borrado el usuario correctamente
     */
    private void informarUsuarioBorrado() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Usuario borrado correctamente", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    /* informa mediante un TOAST de que se ha actualizado la contraseña correctamente
     */
    private void informarContrasenaCambiada() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Contraseña actualizada correctamente", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }


    /* informa mediante un TOAST de que falta algún campo por rellenar
     */
    private void informarRellenarDatos() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Por favor, rellena todos los campos", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    /* informa mediante un TOAST de que ha habido un fallo desconocido
     */
    private void informarFalloDesconocido() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Ha ocurrido un fallo desconocido", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    /* informa mediante un TOAST de que las contraseñas no coinciden
     */
    private void informarContrasenasNoCoinciden() {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Cambiar color del fonto
        view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_perfil, container, false);
    }
}
