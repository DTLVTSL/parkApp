package com.example.daniel.firebaseauth;


import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by Jessica on 22/04/17.
 */

public class statistics extends Fragment {
        public void voidViewCreated(View view, @Nullable Bundle saveInstanceState){
            super.onViewCreated(view,saveInstanceState);
            getActivity().setTitle("I miei Statische");

        }
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            return  inflater.inflate(R.layout.statistics,container,false);
        }
}
