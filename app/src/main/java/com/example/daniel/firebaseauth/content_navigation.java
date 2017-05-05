package com.example.daniel.firebaseauth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jessica on 4/05/17.
 */

public class content_navigation extends Fragment{

    Button button2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.content_navigation, container, false);
        button2 = (Button) view.findViewById(R.id.button2);

        /*
        initData();

        ListView lv = (ListView) view.findViewById(R.id.lv_main);

        adapter = new AdapterAllGroup(getActivity(), groupBalance);
        lv.setAdapter(adapter);
        */


        return view;
    }
    private void onClick_nci()

    {
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        startActivity(intent);

    }


}
