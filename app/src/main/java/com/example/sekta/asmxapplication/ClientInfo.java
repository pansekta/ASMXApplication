package com.example.sekta.asmxapplication;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Sekta on 2015-11-01.
 */
public class ClientInfo {
    int Id;
    String FirstName;
    String LastName ;
    String Email;
    String Phone;
    int YearlyIncome;
    String JobPosition;
    String Company;
    String PersonalData;
    String MonthsWithContract;

    public ClientInfo(SoapObject object) {
        new Deserialization().SoapDeserialize(this, object);
    }
}
