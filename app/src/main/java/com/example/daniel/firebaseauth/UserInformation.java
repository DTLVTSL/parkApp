package com.example.daniel.firebaseauth;

/**
 * Created by daniel on 4/10/2017.
 */

public class UserInformation {

    public String name;
    public String Surname;
    public String CodiceFiscale;
    public String datebirth;
    public String gender;
    public String CodiceMedico;
   // public String userId;

    public UserInformation(){

    }




    public UserInformation(String name, String Surname, String CodiceFiscale, String datebirth, String gender, String CodiceMedico) {
        this.name = name;
        this.Surname = Surname;
        this.CodiceFiscale = CodiceFiscale;
        this.datebirth = datebirth;
        this.gender = gender;
        this.CodiceMedico = CodiceMedico;
       // this.userId = userId;
    }
    public String getCodMedico() {
        return CodiceMedico;
    }
    //public String getCodiceMedico (){
      //      return CodiceMedico;
   // }

}