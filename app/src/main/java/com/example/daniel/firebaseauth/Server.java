package com.example.daniel.firebaseauth;

/**
 * Created by daniel on 7/7/2017.
 */

public class Server {
    String IP;

    public Server(){

    }

    public Server(String IP){
        this.IP=IP;
    }
    public String getIP(){
        return IP;
    }
}
