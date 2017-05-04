package com.example.daniel.firebaseauth;

/**
 * Created by daniel on 4/10/2017.
 */

public class UserInformation {

    public String name;
    public String Surname;
    public String CodiceFiscale;
    public String datebirth;
    public String userId;

    public UserInformation(){

    }


    public UserInformation(String name, String Surname, String CodiceFiscale, String datebirth,String userId) {
        this.name = name;
        this.Surname = Surname;
        this.CodiceFiscale = CodiceFiscale;
        this.datebirth = datebirth;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return name;
    }

    public String getUserSurname() {
        return Surname;
    }
}