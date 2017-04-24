package com.example.daniel.firebaseauth;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;

import java.util.zip.Inflater;

/**
 * Created by Jessica on 22/04/17.
 */

public class Perfil extends Fragment{
    public void voidViewCreated(View view, @Nullable Bundle saveInstanceState){
            super.onViewCreated(view,saveInstanceState);
            getActivity().setTitle("Mio profilo");

    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return  inflater.inflate(R.layout.perfil,container,false);
    }
}
