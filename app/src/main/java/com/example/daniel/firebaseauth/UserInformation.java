package com.example.daniel.firebaseauth;

/**
 * Created by daniel on 4/10/2017.
 */

public class UserInformation {

    public String name;
    public String Surname;
    public String CodiceFiscale;
    public String datebirth;

    public UserInformation(){

    }


    public UserInformation(String name, String Surname, String CodiceFiscale, String datebirth) {
        this.name = name;
        this.Surname = Surname;
        this.CodiceFiscale = CodiceFiscale;
        this.datebirth = datebirth;
    }
}