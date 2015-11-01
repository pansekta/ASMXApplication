package com.example.sekta.asmxapplication;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Sekta on 2015-11-01.
 */
public class User {
    int Id;
    String FirstName;
    String LastName;
    String Email;
    String Login;
    String Password;
    public User(SoapObject object) {
        new Deserialization().SoapDeserialize(this, object);
    }
}
