package com.example.carolshaw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnadirPodcastALista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnadirPodcastALista extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;

    public AnadirPodcastALista() {
        // Required empty public constructor
    }

    public static AnadirPodcastALista newInstance(int param1) {
        AnadirPodcastALista fragment = new AnadirPodcastALista();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anadir_podcast_a_lista, container, false);
    }
}
